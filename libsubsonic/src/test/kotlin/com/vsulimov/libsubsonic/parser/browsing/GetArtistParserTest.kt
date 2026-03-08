package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetArtistParserTest {

    @Test
    fun `parse handles detailed artist and album metadata from navidrome`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
                "openSubsonic": true,
                "artist": {
                    "id": "art1",
                    "name": "Bring Me The Horizon",
                    "coverArt": "ca1",
                    "albumCount": 1,
                    "artistImageUrl": "http://img.com/artist",
                    "sortName": "bring me the horizon",
                    "roles": ["artist", "albumartist"],
                    "album": [
                        {
                            "id": "alb1",
                            "name": "Sempiternal",
                            "artist": "Bring Me The Horizon",
                            "artistId": "art1",
                            "coverArt": "ca2",
                            "songCount": 14,
                            "duration": 3460,
                            "playCount": 6,
                            "created": "2025-12-30T00:30:33Z",
                            "year": 2013,
                            "genre": "Alternative Rock",
                            "played": "2026-01-05T14:09:34Z",
                            "userRating": 0,
                            "isCompilation": false,
                            "sortName": "sempiternal",
                            "displayArtist": "Bring Me The Horizon"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetArtistParser.parse(JSONObject(jsonString))

        TestFixtures.assertNavidromeMetadata(response)

        val artist = response.artist
        assertEquals("art1", artist.id)
        assertEquals("bring me the horizon", artist.sortName)
        assertEquals(listOf("artist", "albumartist"), artist.roles)
        assertEquals(1, artist.albums.size)

        val album = artist.albums[0]
        assertEquals("alb1", album.id)
        assertEquals("Sempiternal", album.name)
        assertEquals(6, album.playCount)
        assertEquals(0, album.userRating)
        assertEquals(false, album.isCompilation)
        assertEquals("sempiternal", album.sortName)
        assertEquals("Bring Me The Horizon", album.displayArtist)
    }

    @Test
    fun `parse handles artist with no albums`() {
        val jsonString = """
            {
                "status": "ok",
                "artist": {
                    "id": "art2",
                    "name": "New Artist",
                    "albumCount": 0
                }
            }
        """.trimIndent()

        val response = GetArtistParser.parse(JSONObject(jsonString))

        assertEquals("art2", response.artist.id)
        assertEquals("New Artist", response.artist.name)
        assertTrue(response.artist.albums.isEmpty())
    }

    @Test
    fun `parse handles single album object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "artist": {
                    "id": "art3",
                    "name": "Solo Artist",
                    "albumCount": 1,
                    "album": {
                        "id": "alb2",
                        "name": "Debut",
                        "artistId": "art3"
                    }
                }
            }
        """.trimIndent()

        val response = GetArtistParser.parse(JSONObject(jsonString))

        assertEquals(1, response.artist.albums.size)
        assertEquals("alb2", response.artist.albums[0].id)
        assertEquals("Debut", response.artist.albums[0].name)
    }

    @Test
    fun `parse returns default artist when artist key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetArtistParser.parse(JSONObject(jsonString))

        assertEquals("", response.artist.id)
        assertEquals("", response.artist.name)
        assertTrue(response.artist.albums.isEmpty())
    }

    @Test
    fun `parse handles artist without optional fields`() {
        val jsonString = """
            {
                "status": "ok",
                "artist": {
                    "id": "art4",
                    "name": "Minimal Artist"
                }
            }
        """.trimIndent()

        val response = GetArtistParser.parse(JSONObject(jsonString))

        val artist = response.artist
        assertEquals("art4", artist.id)
        assertNull(artist.coverArt)
        assertNull(artist.artistImageUrl)
        assertNull(artist.musicBrainzId)
        assertNull(artist.sortName)
        assertTrue(artist.roles.isEmpty())
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetArtistParser.parse(
            TestFixtures.navidromeResponseJson("artist", """{"id": "x", "name": "X", "album": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
