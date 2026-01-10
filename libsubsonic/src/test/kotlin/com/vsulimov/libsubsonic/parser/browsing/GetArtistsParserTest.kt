package com.vsulimov.libsubsonic.parser.browsing

import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class GetArtistsParserTest {

    @Test
    fun `parse handles ID3 artists with counts and images`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0 (cc3cca60)",
                "openSubsonic": true,
                "artists": {
                    "lastModified": 1767054630000,
                    "ignoredArticles": "The El La Los Las Le Les Os As O A",
                    "index": [
                        {
                            "name": "A",
                            "artist": [
                                {
                                    "id": "a1",
                                    "name": "All Good Things",
                                    "coverArt": "ca1",
                                    "albumCount": 1,
                                    "artistImageUrl": "http://img.com/1",
                                    "musicBrainzId": "mb1",
                                    "sortName": "all good things",
                                    "roles": ["albumartist", "artist"]
                                }
                            ]
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetArtistsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.16.1", response.apiVersion)
        assertEquals(1767054630000L, response.lastModified)

        val result = response.artists
        assertEquals(expected = 1, actual = result.size)
        val artist = result[0].artists[0]
        assertEquals(expected = "All Good Things", actual = artist.name)
        assertEquals(expected = 1, actual = artist.albumCount)
        assertEquals(expected = "ca1", actual = artist.coverArt)
        assertEquals(expected = "mb1", actual = artist.musicBrainzId)
        assertEquals(expected = "all good things", actual = artist.sortName)
        assertEquals(expected = listOf("albumartist", "artist"), actual = artist.roles)
    }
}
