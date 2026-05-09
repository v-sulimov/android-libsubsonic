package com.vsulimov.libsubsonic.auth

import com.vsulimov.libsubsonic.data.Constants.DEFAULT_API_VERSION
import com.vsulimov.libsubsonic.data.Constants.DEFAULT_CLIENT_NAME
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Generates Subsonic authentication parameters using the token-and-salt scheme.
 *
 * Following the Subsonic REST API guidelines, the token (`t`) is the MD5 hash of the
 * clear-text password concatenated with a random salt (`s`). A fresh salt is generated
 * for every request so the password is never transmitted and replay attacks are mitigated.
 *
 * @property username The Subsonic username, sent as the `u` parameter.
 * @property password The clear-text password, used locally to compute the MD5 token.
 * @property clientName The client identifier, sent as the `c` parameter.
 * @property apiVersion The Subsonic REST API version, sent as the `v` parameter.
 */
internal class SubsonicAuthenticator(
    private val username: String,
    private val password: String,
    private val clientName: String = DEFAULT_CLIENT_NAME,
    private val apiVersion: String = DEFAULT_API_VERSION
) {

    private val secureRandom = SecureRandom()

    /**
     * Generates a fresh set of authentication parameters for a single API request.
     *
     * Must be invoked once per request so that each request carries a unique salt.
     *
     * @return A map with the keys `u` (username), `s` (salt), `t` (MD5 token),
     *   `v` (API version) and `c` (client name).
     */
    fun generateAuthParams(): Map<String, String> {
        val salt = generateRandomSalt()
        val token = calculateToken(password, salt)
        return mapOf(
            "u" to username,
            "s" to salt,
            "t" to token,
            "v" to apiVersion,
            "c" to clientName
        )
    }

    /**
     * Generates a cryptographically secure random salt encoded as a lowercase hex string.
     *
     * @return A lowercase hex string derived from [SALT_BYTE_SIZE] random bytes.
     */
    private fun generateRandomSalt(): String {
        val bytes = ByteArray(SALT_BYTE_SIZE)
        secureRandom.nextBytes(bytes)
        return bytes.toHexString()
    }

    /**
     * Computes the Subsonic authentication token as `md5(password + salt)`.
     *
     * @param password The clear-text password.
     * @param salt The unique salt generated for this request.
     * @return The lowercase MD5 hex digest of the concatenated bytes.
     */
    private fun calculateToken(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val bytes = digest.digest((password + salt).toByteArray(Charsets.UTF_8))
        return bytes.toHexString()
    }

    private companion object {
        /** Salt entropy in bytes; 4 bytes encode to an 8-character hex string. */
        const val SALT_BYTE_SIZE = 4
    }
}
