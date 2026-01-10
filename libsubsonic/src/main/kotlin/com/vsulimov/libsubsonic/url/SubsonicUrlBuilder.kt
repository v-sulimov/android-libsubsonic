package com.vsulimov.libsubsonic.url

import com.vsulimov.libsubsonic.auth.SubsonicAuthenticator
import java.net.URLEncoder

/**
 * Builds fully qualified Subsonic REST API URLs.
 *
 * This component holds the base URL and injects authentication parameters
 * (username, token, salt) into every request.
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
        authenticator = SubsonicAuthenticator(
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
     * 4. The provided API-specific [multiValueParams], where each key may appear multiple times
     *    in the query string — once per value (e.g., `songId=1&songId=2&songId=3`).
     * 5. The authentication parameters (`u`, `t`, `s`, `v`, `c`).
     *
     * @param endpoint The API method name (e.g., "ping").
     * @param params Optional single-value query parameters for the specific API call.
     * @param multiValueParams Optional multi-value query parameters where a single key maps to
     *   a list of values, each appended as a separate query pair.
     * @return The fully qualified URL string.
     * @throws IllegalStateException If credentials have not been set.
     */
    fun buildUrl(
        endpoint: String,
        params: Map<String, String> = emptyMap(),
        multiValueParams: Map<String, List<String>> = emptyMap()
    ): String {
        val authParams = getAuthParamsOrThrow()

        val saneBaseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        val stringBuilder = StringBuilder(saneBaseUrl)
        stringBuilder.append("rest/").append(endpoint).append("?")
        stringBuilder.append("f=json")

        params.forEach { (key, value) ->
            stringBuilder.append("&")
                .append(key)
                .append("=")
                .append(URLEncoder.encode(value, "UTF-8"))
        }

        multiValueParams.forEach { (key, values) ->
            values.forEach { value ->
                stringBuilder.append("&")
                    .append(key)
                    .append("=")
                    .append(URLEncoder.encode(value, "UTF-8"))
            }
        }

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
