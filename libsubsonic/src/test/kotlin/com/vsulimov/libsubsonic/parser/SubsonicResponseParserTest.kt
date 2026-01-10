package com.vsulimov.libsubsonic.parser

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
}
