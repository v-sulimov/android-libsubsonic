package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetArtistsParserTest {

    @Test
    fun `parse handles ID3 artists with counts and images`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
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

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(expected = 1, actual = response.artists.size)

        val artist = response.artists[0].artists[0]
        assertEquals(expected = "All Good Things", actual = artist.name)
        assertEquals(expected = 1, actual = artist.albumCount)
        assertEquals(expected = "ca1", actual = artist.coverArt)
        assertEquals(expected = "mb1", actual = artist.musicBrainzId)
        assertEquals(expected = "all good things", actual = artist.sortName)
        assertEquals(expected = listOf("albumartist", "artist"), actual = artist.roles)
        assertNull(artist.starred)
    }

    @Test
    fun `parse captures starred timestamp`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "artists": {
                    "ignoredArticles": "",
                    "index": [
                        {
                            "name": "I",
                            "artist": [
                                {
                                    "id": "126",
                                    "name": "Iron Maiden",
                                    "coverArt": "ar-126",
                                    "albumCount": 1,
                                    "starred": "2012-04-05T19:03:31"
                                },
                                {
                                    "id": "141",
                                    "name": "Kvelertak",
                                    "albumCount": 1
                                }
                            ]
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetArtistsParser.parse(JSONObject(jsonString))

        val artists = response.artists[0].artists
        assertEquals("2012-04-05T19:03:31", artists[0].starred)
        assertNull(artists[1].starred)
    }

    @Test
    fun `parse returns empty list when artists key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetArtistsParser.parse(JSONObject(jsonString))

        assertTrue(response.artists.isEmpty())
    }

    @Test
    fun `parse handles single index object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "artists": {
                    "ignoredArticles": "",
                    "index": {
                        "name": "B",
                        "artist": [
                            { "id": "b1", "name": "Beatles", "albumCount": 13 }
                        ]
                    }
                }
            }
        """.trimIndent()

        val response = GetArtistsParser.parse(JSONObject(jsonString))

        assertEquals(1, response.artists.size)
        assertEquals("B", response.artists[0].name)
        assertEquals("Beatles", response.artists[0].artists[0].name)
    }

    @Test
    fun `parse handles single artist inside index instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "artists": {
                    "ignoredArticles": "",
                    "index": [
                        {
                            "name": "M",
                            "artist": { "id": "m1", "name": "Metallica", "albumCount": 10 }
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetArtistsParser.parse(JSONObject(jsonString))

        assertEquals(1, response.artists[0].artists.size)
        assertEquals("Metallica", response.artists[0].artists[0].name)
    }

    @Test
    fun `parse handles multiple indexes`() {
        val jsonString = """
            {
                "status": "ok",
                "artists": {
                    "ignoredArticles": "",
                    "index": [
                        {
                            "name": "A",
                            "artist": [{ "id": "a1", "name": "AC/DC", "albumCount": 5 }]
                        },
                        {
                            "name": "B",
                            "artist": [{ "id": "b1", "name": "Beatles", "albumCount": 13 }]
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetArtistsParser.parse(JSONObject(jsonString))

        assertEquals(2, response.artists.size)
        assertEquals("A", response.artists[0].name)
        assertEquals("B", response.artists[1].name)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetArtistsParser.parse(
            TestFixtures.navidromeResponseJson("artists", """{"ignoredArticles": "", "index": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertTrue(response.artists.isEmpty())
    }
}
