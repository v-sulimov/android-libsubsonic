package com.vsulimov.libsubsonic.parser.scan

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetScanStatusParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.15.0",
                "scanStatus": {
                    "scanning": false
                }
            }
        """.trimIndent()

        val response = GetScanStatusParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.15.0", response.apiVersion)
    }

    @Test
    fun `parse extracts scanning true and count`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.15.0",
                "scanStatus": {
                    "scanning": true,
                    "count": 5422
                }
            }
        """.trimIndent()

        val response = GetScanStatusParser.parse(JSONObject(jsonString))

        assertTrue(response.scanStatus.scanning)
        assertEquals(5422L, response.scanStatus.count)
    }

    @Test
    fun `parse sets count to null when absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.15.0",
                "scanStatus": {
                    "scanning": false
                }
            }
        """.trimIndent()

        val response = GetScanStatusParser.parse(JSONObject(jsonString))

        assertNull(response.scanStatus.count)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetScanStatusParser.parse(
            TestFixtures.navidromeResponseJson("scanStatus", "{\"scanning\":false}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
