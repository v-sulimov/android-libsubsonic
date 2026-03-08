package com.vsulimov.libsubsonic.parser.lyrics

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetLyricsParserTest {

    @Test
    fun `parse extracts lyrics fields correctly`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.2.0",
                "lyrics": {
                    "artist": "Muse",
                    "title": "Hysteria",
                    "value": "It's bugging me"
                }
            }
        """.trimIndent()

        val response = GetLyricsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.2.0", response.apiVersion)
        assertEquals("Muse", response.lyrics.artist)
        assertEquals("Hysteria", response.lyrics.title)
        assertEquals("It's bugging me", response.lyrics.value)
    }

    @Test
    fun `parse handles missing optional lyrics fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.2.0",
                "lyrics": {}
            }
        """.trimIndent()

        val response = GetLyricsParser.parse(JSONObject(jsonString))

        assertNull(response.lyrics.artist)
        assertNull(response.lyrics.title)
        assertNull(response.lyrics.value)
    }

    @Test
    fun `parse returns empty lyrics when lyrics object is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.2.0"
            }
        """.trimIndent()

        val response = GetLyricsParser.parse(JSONObject(jsonString))

        assertNull(response.lyrics.artist)
        assertNull(response.lyrics.title)
        assertNull(response.lyrics.value)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetLyricsParser.parse(
            TestFixtures.navidromeResponseJson("lyrics", """{"artist": "Muse", "title": "Hysteria"}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals("Muse", response.lyrics.artist)
        assertEquals("Hysteria", response.lyrics.title)
    }
}
