package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class DeletePlaylistParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = DeletePlaylistParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.16.1", response.apiVersion)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = DeletePlaylistParser.parse(
            TestFixtures.navidromeResponseJson("ignored", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
