package com.vsulimov.libsubsonic.url

import com.vsulimov.libsubsonic.auth.SubsonicAuthenticator
import java.net.URLEncoder

/**
 * This class is responsible for constructing valid Subsonic API URLs.
 *
 * It manages the base URL and injects the necessary authentication parameters
 * (username, salt, token) into every request query.
 *
 * @property baseUrl The root URL of the Subsonic server.
 * @property clientName The client identifier used for the `c` parameter.
 */
internal class SubsonicUrlBuilder(private val baseUrl: String, private val clientName: String) {

    private var authenticator: SubsonicAuthenticator? = null

    /**
     * Updates the internal authenticator with new credentials.
     *
     * @param username The Subsonic username.
     * @param password The clear-text password.
     */
    fun setCredentials(username: String, password: String) {
        this.authenticator = SubsonicAuthenticator(
            username = username,
            password = password,
            clientName = clientName
        )
    }

    /**
     * Constructs a full URL for a specific API endpoint.
     *
     * This method appends:
     * 1. The REST API path (e.g., `/rest/ping`).
     * 2. The mandatory `f=json` parameter.
     * 3. The provided API-specific [params].
     * 4. The authentication parameters (`u`, `t`, `s`, `v`, `c`).
     *
     * @param endpoint The API method name (e.g., "ping").
     * @param params Optional query parameters for the specific API call.
     * @return The fully qualified URL string.
     * @throws IllegalStateException If credentials have not been set.
     */
    fun buildUrl(endpoint: String, params: Map<String, String> = emptyMap()): String {
        val authParams = getAuthParamsOrThrow()

        // Ensure base URL ends with a slash for consistent joining
        val saneBaseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

        val stringBuilder = StringBuilder(saneBaseUrl)
        stringBuilder.append("rest/").append(endpoint).append("?")

        // Force JSON format
        stringBuilder.append("f=json")

        // Append API-specific Params
        params.forEach { (key, value) ->
            stringBuilder.append("&")
                .append(key)
                .append("=")
                .append(URLEncoder.encode(value, "UTF-8"))
        }

        // Append Auth Params
        authParams.forEach { (key, value) ->
            stringBuilder.append("&")
                .append(key)
                .append("=")
                .append(URLEncoder.encode(value, "UTF-8"))
        }

        return stringBuilder.toString()
    }

    /**
     * Retrieves the authentication parameters or throws if not initialized.
     */
    private fun getAuthParamsOrThrow(): Map<String, String> = authenticator?.generateAuthParams()
        ?: throw IllegalStateException("Credentials not set. Call setCredentials() first.")
}
