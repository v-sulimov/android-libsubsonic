package com.vsulimov.libsubsonic.parser

import com.vsulimov.libsubsonic.data.Constants.DEFAULT_ERROR_CODE
import com.vsulimov.libsubsonic.data.result.SubsonicResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubsonicResponseParserTest {

    private val parser = SubsonicResponseParser()

    @Test
    fun `parse success state returns data`() {
        val json = """{"subsonic-response": {"status":"ok", "version":"1.0", "mock": "value"}}"""

        val result = parser.parse(json) { it.getString("mock") }

        assertTrue(actual = result is SubsonicResult.Success)
        assertEquals(expected = "value", actual = result.data)
    }

    @Test
    fun `parse failure state returns mapped SubsonicError`() {
        val json = """
            {
                "subsonic-response": {
                    "status": "failed",
                    "error": {"code": 40, "message": "Wrong password"}
                }
            }
        """.trimIndent()

        val result = parser.parse(jsonString = json) { "nothing" }

        assertTrue(actual = result is SubsonicResult.Failure)
        assertEquals(expected = 40, actual = result.error.code)
        assertEquals(expected = "Wrong password", actual = result.error.message)
    }

    @Test
    fun `parse malformed json returns failure`() {
        val result = parser.parse("{{") { "nothing" }

        assertTrue(result is SubsonicResult.Failure)
        assertTrue(result.error.message.contains("JSON"))
    }

    @Test
    fun `parse blank string returns failure with descriptive message`() {
        val result = parser.parse("   ") { "nothing" }

        assertTrue(result is SubsonicResult.Failure)
        assertEquals(expected = DEFAULT_ERROR_CODE, actual = result.error.code)
        assertEquals(expected = "Empty response received", actual = result.error.message)
    }

    @Test
    fun `parse missing subsonic-response key returns failure`() {
        val result = parser.parse("""{"data": "value"}""") { "nothing" }

        assertTrue(result is SubsonicResult.Failure)
        assertEquals(expected = DEFAULT_ERROR_CODE, actual = result.error.code)
        assertTrue(result.error.message.contains("subsonic-response"))
    }

    @Test
    fun `parse failure without error block uses default code and message`() {
        val json = """{"subsonic-response": {"status": "failed"}}"""

        val result = parser.parse(json) { "nothing" }

        assertTrue(result is SubsonicResult.Failure)
        assertEquals(expected = DEFAULT_ERROR_CODE, actual = result.error.code)
        assertEquals(expected = "Unknown API error", actual = result.error.message)
    }

    @Test
    fun `parse failure with error block missing code uses default code`() {
        val json = """
            {
                "subsonic-response": {
                    "status": "failed",
                    "error": {"message": "Access denied"}
                }
            }
        """.trimIndent()

        val result = parser.parse(json) { "nothing" }

        assertTrue(result is SubsonicResult.Failure)
        assertEquals(expected = DEFAULT_ERROR_CODE, actual = result.error.code)
        assertEquals(expected = "Access denied", actual = result.error.message)
    }

    @Test
    fun `parse failure with error block missing message returns empty string`() {
        val json = """
            {
                "subsonic-response": {
                    "status": "failed",
                    "error": {"code": 50}
                }
            }
        """.trimIndent()

        val result = parser.parse(json) { "nothing" }

        assertTrue(result is SubsonicResult.Failure)
        assertEquals(expected = 50, actual = result.error.code)
        assertEquals(expected = "", actual = result.error.message)
    }

    @Test
    fun `parse exception thrown by parser lambda returns failure`() {
        val json = """{"subsonic-response": {"status": "ok", "version": "1.0"}}"""

        val result = parser.parse(json) { throw RuntimeException("parser crash") }

        assertTrue(result is SubsonicResult.Failure)
        assertTrue(result.error.message.contains("parser crash"))
    }
}
