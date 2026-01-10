package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetAlbumInfoParserTest {

    @Test
    fun `parse handles album info with notes and urls`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
                "openSubsonic": true,
                "albumInfo": {
                    "notes": "A landmark release.",
                    "musicBrainzId": "mb-alb-1",
                    "lastFmUrl": "https://www.last.fm/music/Example/Album",
                    "smallImageUrl": "https://img.com/small.jpg",
                    "mediumImageUrl": "https://img.com/medium.jpg",
                    "largeImageUrl": "https://img.com/large.jpg"
                }
            }
        """.trimIndent()

        val response = GetAlbumInfoParser.parse(JSONObject(jsonString))

        TestFixtures.assertNavidromeMetadata(response)

        val info = response.albumInfo
        assertEquals("A landmark release.", info.notes)
        assertEquals("mb-alb-1", info.musicBrainzId)
        assertEquals("https://www.last.fm/music/Example/Album", info.lastFmUrl)
        assertEquals("https://img.com/small.jpg", info.smallImageUrl)
        assertEquals("https://img.com/medium.jpg", info.mediumImageUrl)
        assertEquals("https://img.com/large.jpg", info.largeImageUrl)
    }

    @Test
    fun `parse handles missing album info fields`() {
        val jsonString = """
            {
                "status": "ok",
                "albumInfo": {}
            }
        """.trimIndent()

        val response = GetAlbumInfoParser.parse(JSONObject(jsonString))

        assertNull(response.albumInfo.notes)
        assertNull(response.albumInfo.lastFmUrl)
    }

    @Test
    fun `parse returns default album info when albumInfo key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetAlbumInfoParser.parse(JSONObject(jsonString))

        assertNull(response.albumInfo.notes)
        assertNull(response.albumInfo.musicBrainzId)
        assertNull(response.albumInfo.lastFmUrl)
        assertNull(response.albumInfo.smallImageUrl)
        assertNull(response.albumInfo.mediumImageUrl)
        assertNull(response.albumInfo.largeImageUrl)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetAlbumInfoParser.parse(
            TestFixtures.navidromeResponseJson("albumInfo", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
