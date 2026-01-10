package com.vsulimov.libsubsonic.parser.system

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import org.json.JSONObject

class PingParserTest {

    @Test
    fun `parse extracts all fields correctly when present`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
                "openSubsonic": true
            }
        """.trimIndent()

        val response = PingParser.parse(JSONObject(jsonString))

        TestFixtures.assertNavidromeMetadata(response)
    }

    @Test
    fun `parse handles missing optional fields with defaults`() {
        val response = PingParser.parse(JSONObject("{}"))

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

        val response = PingParser.parse(JSONObject(jsonString))

        assertEquals(expected = "ok", actual = response.status)
        assertEquals(expected = "1.16.1", actual = response.apiVersion)
        assertNull(actual = response.serverType)
        assertNull(actual = response.serverVersion)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = PingParser.parse(TestFixtures.navidromeResponseJson("ping"))

        TestFixtures.assertNavidromeMetadata(response)
    }
}
