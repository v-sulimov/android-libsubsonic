package com.vsulimov.libsubsonic.auth

import com.vsulimov.libsubsonic.auth.SubsonicAuthenticator.Companion.SALT_BYTE_SIZE
import com.vsulimov.libsubsonic.data.Constants.DEFAULT_API_VERSION
import com.vsulimov.libsubsonic.data.Constants.DEFAULT_CLIENT_NAME
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Generates Subsonic authentication parameters using the token-and-salt strategy.
 *
 * This implementation follows the Subsonic REST API guidelines, where the token (t)
 * is computed as the MD5 hash of the clear-text password concatenated with a random
 * salt (s). This prevents sending the password directly over the network.
 *
 * @property username The Subsonic username, passed as the `u` parameter.
 * @property password The clear-text password, used locally to generate the MD5 token.
 * @property clientName The client identifier, passed as the `c` parameter.
 * @property apiVersion The Subsonic REST API version, passed as the `v` parameter.
 */
class SubsonicAuthenticator(
    private val username: String,
    private val password: String,
    private val clientName: String = DEFAULT_CLIENT_NAME,
    private val apiVersion: String = DEFAULT_API_VERSION
) {

    private val secureRandom = SecureRandom()

    /**
     * Internal configuration for hex string formatting.
     * Configured to output lowercase hex without prefixes or delimiters.
     */
    private val hexFormat = HexFormat { upperCase = false }

    /**
     * Generates a set of authentication parameters for an API request.
     *
     * This should be called for every new request to ensure that a unique salt
     * is used, which mitigates the risk of replay attacks.
     *
     * @return A [Map] containing the keys:
     * - `u`: The username.
     * - `s`: A unique, random salt.
     * - `t`: The computed MD5 token.
     * - `v`: The API version.
     * - `c`: The client name.
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
     * Generates a cryptographically secure random hex string to be used as a salt.
     *
     * @return A lowercase hex string derived from [SALT_BYTE_SIZE] random bytes.
     */
    private fun generateRandomSalt(): String {
        val byteArray = ByteArray(SALT_BYTE_SIZE)
        secureRandom.nextBytes(byteArray)
        return byteArray.toHexString(hexFormat)
    }

    /**
     * Computes the Subsonic authentication token.
     *
     * The token is calculated by taking the MD5 digest of the string formed by
     * appending the salt to the clear-text password.
     *
     * @param password The clear-text password.
     * @param salt The unique salt generated for this request.
     * @return A lowercase MD5 hex string representing the authentication token.
     */
    private fun calculateToken(password: String, salt: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        val input = password + salt
        val hashBytes = messageDigest.digest(input.toByteArray(Charsets.UTF_8))
        return hashBytes.toHexString(hexFormat)
    }

    companion object {
        /**
         * The entropy size for salt generation.
         * 4 bytes of entropy results in an 8-character hex string.
         */
        private const val SALT_BYTE_SIZE = 4
    }
}
