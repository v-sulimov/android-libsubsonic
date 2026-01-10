package com.vsulimov.libsubsonic.auth

import com.vsulimov.libsubsonic.data.TestConstants.DEFAULT_API_VERSION
import com.vsulimov.libsubsonic.data.TestConstants.DEFAULT_CLIENT_NAME
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

        val messageDigest = MessageDigest.getInstance("MD5")
        val hashBytes = messageDigest.digest((password + salt).toByteArray(Charsets.UTF_8))
        val expectedToken = hashBytes.joinToString("") { "%02x".format(it) }

        assertEquals(expected = expectedToken, actual = token)
    }

    @Test
    fun `token format is exactly 32 lowercase hex characters`() {
        val params = authenticator.generateAuthParams()
        val token = params["t"] ?: ""

        assertEquals(expected = 32, actual = token.length)
        assertTrue(actual = token.matches(Regex("^[a-f0-9]+$")))
    }

    @Test
    fun `salts are unique for every call`() {
        val firstCall = authenticator.generateAuthParams()
        val secondCall = authenticator.generateAuthParams()

        assertNotEquals(illegal = firstCall["s"], actual = secondCall["s"])
        assertNotEquals(illegal = firstCall["t"], actual = secondCall["t"])
    }

    @Test
    fun `salt format is exactly 8 lowercase hex characters`() {
        val params = authenticator.generateAuthParams()
        val salt = params["s"] ?: ""

        assertEquals(expected = 8, actual = salt.length)
        assertTrue(actual = salt.matches(Regex("^[a-f0-9]+$")))
    }
}
