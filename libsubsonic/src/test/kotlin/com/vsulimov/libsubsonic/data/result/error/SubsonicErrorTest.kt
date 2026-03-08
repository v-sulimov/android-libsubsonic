package com.vsulimov.libsubsonic.data.result.error

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubsonicErrorTest {

    @Test
    fun `error preserves code and message`() {
        val error = SubsonicError(code = SubsonicErrorCode.WRONG_CREDENTIALS, message = "Wrong password")

        assertEquals(SubsonicErrorCode.WRONG_CREDENTIALS, error.code)
        assertEquals("Wrong password", error.message)
    }

    @Test
    fun `error skips stack trace capture`() {
        val error = SubsonicError(code = SubsonicErrorCode.GENERIC_ERROR, message = "test")

        assertTrue(error.stackTrace.isEmpty())
    }

    @Test
    fun `error equality works as data class`() {
        val a = SubsonicError(code = SubsonicErrorCode.WRONG_CREDENTIALS, message = "Wrong password")
        val b = SubsonicError(code = SubsonicErrorCode.WRONG_CREDENTIALS, message = "Wrong password")

        assertEquals(a, b)
    }

    @Test
    fun `error is throwable`() {
        val error: Throwable = SubsonicError(code = SubsonicErrorCode.NOT_AUTHORIZED, message = "Permission denied")

        assertTrue(error is Exception)
        assertEquals("Permission denied", error.message)
    }
}
