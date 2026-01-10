package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetStarredParserTest {

    @Test
    fun `parse returns artists albums and songs from starred2 container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "starred2": {
                    "artist": [
                        {
                            "id": "126",
                            "name": "Iron Maiden",
                            "coverArt": "ar-126",
                            "albumCount": 1,
                            "starred": "2012-04-05T19:03:31"
                        },
                        {
                            "id": "133",
                            "name": "Dimmu Borgir",
                            "coverArt": "ar-133",
                            "albumCount": 1,
                            "starred": "2012-04-05T19:03:17"
                        }
                    ],
                    "album": [
                        {
                            "id": "180",
                            "name": "Collapse Into Now",
                            "artist": "R.E.M.",
                            "artistId": "144",
                            "songCount": 12,
                            "duration": 2459,
                            "created": "2011-03-23T09:37:55",
                            "starred": "2012-04-05T19:02:02"
                        }
                    ],
                    "song": [
                        {
                            "id": "143588",
                            "parent": "143586",
                            "title": "Born Treacherous",
                            "isDir": false,
                            "album": "Abrahadabra",
                            "artist": "Dimmu Borgir",
                            "duration": 302,
                            "starred": "2012-04-02T17:17:01",
                            "type": "music"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetStarredParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.8.0", response.apiVersion)

        assertEquals(2, response.artists.size)
        val firstArtist = response.artists[0]
        assertEquals("126", firstArtist.id)
        assertEquals("Iron Maiden", firstArtist.name)
        assertEquals("ar-126", firstArtist.coverArt)
        assertEquals(1, firstArtist.albumCount)
        assertEquals("2012-04-05T19:03:31", firstArtist.starred)

        val secondArtist = response.artists[1]
        assertEquals("133", secondArtist.id)
        assertEquals("Dimmu Borgir", secondArtist.name)
        assertEquals("2012-04-05T19:03:17", secondArtist.starred)

        assertEquals(1, response.albums.size)
        val album = response.albums[0]
        assertEquals("180", album.id)
        assertEquals("Collapse Into Now", album.name)
        assertEquals("R.E.M.", album.artist)
        assertEquals("144", album.artistId)
        assertEquals(12, album.songCount)
        assertEquals(2459, album.duration)
        assertEquals("2011-03-23T09:37:55", album.created)
        assertEquals("2012-04-05T19:02:02", album.starred)

        assertEquals(1, response.songs.size)
        val song = response.songs[0]
        assertEquals("143588", song.id)
        assertEquals("Born Treacherous", song.title)
        assertEquals(302, song.duration)
        assertEquals("2012-04-02T17:17:01", song.starred)
        assertEquals("music", song.type)
    }

    @Test
    fun `parse returns empty lists when starred2 container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0"
            }
        """.trimIndent()

        val response = GetStarredParser.parse(JSONObject(jsonString))

        assertEquals(0, response.artists.size)
        assertEquals(0, response.albums.size)
        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse returns empty lists when all arrays are absent inside container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "starred2": {}
            }
        """.trimIndent()

        val response = GetStarredParser.parse(JSONObject(jsonString))

        assertEquals(0, response.artists.size)
        assertEquals(0, response.albums.size)
        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse handles single artist object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "starred2": {
                    "artist": {
                        "id": "141",
                        "name": "Kvelertak",
                        "albumCount": 1,
                        "starred": "2012-04-05T19:03:05"
                    }
                }
            }
        """.trimIndent()

        val response = GetStarredParser.parse(JSONObject(jsonString))

        assertEquals(1, response.artists.size)
        assertEquals("141", response.artists[0].id)
        assertEquals("Kvelertak", response.artists[0].name)
        assertEquals("2012-04-05T19:03:05", response.artists[0].starred)
        assertNull(response.artists[0].coverArt)
    }

    @Test
    fun `parse handles single album object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "starred2": {
                    "album": {
                        "id": "178",
                        "name": "Postcards From A Young Man",
                        "artist": "Manic Street Preachers",
                        "artistId": "142",
                        "starred": "2012-04-05T19:01:03"
                    }
                }
            }
        """.trimIndent()

        val response = GetStarredParser.parse(JSONObject(jsonString))

        assertEquals(1, response.albums.size)
        assertEquals("178", response.albums[0].id)
        assertEquals("Postcards From A Young Man", response.albums[0].name)
        assertEquals("2012-04-05T19:01:03", response.albums[0].starred)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetStarredParser.parse(
            TestFixtures.navidromeResponseJson("starred2", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
