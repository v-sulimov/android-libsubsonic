package com.vsulimov.libsubsonic.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PasswordEncoderTest {

    @Test
    fun `encodePassword returns enc prefix followed by hex`() {
        val encoded = encodePassword("sesame")

        assertTrue(encoded.startsWith("enc:"))
    }

    @Test
    fun `encodePassword produces correct hex for known input`() {
        // "sesame" in UTF-8 hex: 736573616d65
        val encoded = encodePassword("sesame")

        assertEquals("enc:736573616d65", encoded)
    }

    @Test
    fun `encodePassword handles empty string`() {
        val encoded = encodePassword("")

        assertEquals("enc:", encoded)
    }

    @Test
    fun `encodePassword handles unicode characters`() {
        val encoded = encodePassword("\u00e9")

        // é in UTF-8 is 0xC3 0xA9
        assertEquals("enc:c3a9", encoded)
    }

    @Test
    fun `encodePassword produces lowercase hex`() {
        val encoded = encodePassword("ABC")

        assertEquals("enc:414243", encoded)
        assertTrue(encoded.removePrefix("enc:").matches(Regex("^[a-f0-9]*$")))
    }
}
