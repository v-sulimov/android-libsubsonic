package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class RefreshPodcastsParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0"
            }
        """.trimIndent()

        val response = RefreshPodcastsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.9.0", response.apiVersion)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = RefreshPodcastsParser.parse(
            TestFixtures.navidromeResponseJson("ignored", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
