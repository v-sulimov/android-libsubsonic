package com.vsulimov.libsubsonic.auth

import com.vsulimov.libsubsonic.Constants.DEFAULT_API_VERSION
import com.vsulimov.libsubsonic.Constants.DEFAULT_CLIENT_NAME
import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class SubsonicAuthenticatorTest {

    private val user = "joe"
    private val password = "sesame"
    private val authenticator = SubsonicAuthenticator(username = user, password = password)

    @Test
    fun `generateAuthParams returns all required subsonic keys with correct values`() {
        val params = authenticator.generateAuthParams()

        assertEquals(expected = user, actual = params["u"])
        assertEquals(expected = DEFAULT_API_VERSION, actual = params["v"])
        assertEquals(expected = DEFAULT_CLIENT_NAME, actual = params["c"])

        assertTrue(actual = params.containsKey("s"))
        assertTrue(actual = params.containsKey("t"))
    }

    @Test
    fun `generateAuthParams uses custom client and version when provided`() {
        val clientName = "Navidrome"
        val apiVersion = "1.16.1"
        val authenticator = SubsonicAuthenticator(
            username = user,
            password = password,
            clientName = clientName,
            apiVersion = apiVersion
        )

        val params = authenticator.generateAuthParams()

        assertEquals(expected = clientName, actual = params["c"])
        assertEquals(expected = apiVersion, actual = params["v"])
    }

    @Test
    fun `token matches MD5 of password plus salt`() {
        val params = authenticator.generateAuthParams()
        val salt = params["s"] ?: ""
        val token = params["t"] ?: ""

        // Manually calculate MD5 to verify logic
        val expectedInput = password + salt
        val messageDigest = MessageDigest.getInstance("MD5")
        val hashBytes = messageDigest.digest(expectedInput.toByteArray(Charsets.UTF_8))

        // Convert to hex string for comparison
        val calculatedHex = hashBytes.joinToString("") { "%02x".format(it) }

        assertEquals(expected = calculatedHex, actual = token)
    }

    @Test
    fun `salts are unique for every call`() {
        val firstCall = authenticator.generateAuthParams()
        val secondCall = authenticator.generateAuthParams()

        val firstSalt = firstCall["s"]
        val secondSalt = secondCall["s"]

        val firstToken = firstCall["t"]
        val secondToken = secondCall["t"]

        assertNotEquals(illegal = firstSalt, actual = secondSalt)
        assertNotEquals(illegal = firstToken, actual = secondToken)
    }

    @Test
    fun `salt format is exactly 8 lowercase hex characters`() {
        val params = authenticator.generateAuthParams()
        val salt = params["s"] ?: ""

        assertEquals(expected = 8, actual = salt.length)
        assertTrue(actual = salt.matches(Regex("^[a-f0-9]+$")))
    }
}
