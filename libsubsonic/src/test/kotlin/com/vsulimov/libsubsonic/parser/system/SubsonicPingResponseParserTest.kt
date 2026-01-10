package com.vsulimov.libsubsonic.parser.system

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class SubsonicPingResponseParserTest {

    @Test
    fun `parse extracts all fields correctly when present`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.16.0",
                "openSubsonic": true
            }
        """.trimIndent()
        val json = JSONObject(jsonString)

        val response = SubsonicPingResponseParser.parse(json)

        assertEquals(expected = "ok", actual = response.status)
        assertEquals(expected = "1.16.1", actual = response.apiVersion)
        assertEquals(expected = "navidrome", actual = response.serverType)
        assertEquals(expected = "0.16.0", actual = response.serverVersion)
        assertTrue(actual = response.isOpenSubsonic)
    }

    @Test
    fun `parse handles missing optional fields with defaults`() {
        val jsonString = "{}"
        val json = JSONObject(jsonString)

        val response = SubsonicPingResponseParser.parse(json)

        assertEquals(expected = "ok", actual = response.status)
        assertEquals(expected = "Unknown", actual = response.apiVersion)
        assertNull(actual = response.serverType)
        assertNull(actual = response.serverVersion)
        assertFalse(actual = response.isOpenSubsonic)
    }

    @Test
    fun `parse handles empty strings as null for type and version`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "",
                "serverVersion": ""
            }
        """.trimIndent()
        val json = JSONObject(jsonString)

        val response = SubsonicPingResponseParser.parse(json)

        assertEquals(expected = "ok", actual = response.status)
        assertEquals(expected = "1.16.1", actual = response.apiVersion)
        assertNull(actual = response.serverType)
        assertNull(actual = response.serverVersion)
    }
}
