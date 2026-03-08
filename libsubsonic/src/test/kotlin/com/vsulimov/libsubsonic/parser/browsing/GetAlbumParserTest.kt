package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetAlbumParserTest {

    @Test
    fun `parse handles detailed album and song metadata from navidrome`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
                "openSubsonic": true,
                "album": {
                    "id": "alb1",
                    "name": "Sempiternal",
                    "artist": "Bring Me The Horizon",
                    "artistId": "art1",
                    "coverArt": "ca1",
                    "songCount": 14,
                    "duration": 3460,
                    "playCount": 6,
                    "created": "2025-12-30T00:30:33Z",
                    "year": 2013,
                    "genre": "Alternative Rock",
                    "played": "2026-01-05T14:09:34Z",
                    "userRating": 5,
                    "isCompilation": false,
                    "sortName": "sempiternal",
                    "displayArtist": "Bring Me The Horizon",
                    "song": [
                        {
                            "id": "song1",
                            "parent": "alb1",
                            "isDir": false,
                            "title": "Can You Feel My Heart",
                            "album": "Sempiternal",
                            "artist": "Bring Me The Horizon",
                            "track": 1,
                            "year": 2013,
                            "genre": "Alternative Rock",
                            "coverArt": "ca1",
                            "size": 8345123,
                            "contentType": "audio/mpeg",
                            "suffix": "mp3",
                            "duration": 227,
                            "bitRate": 320,
                            "path": "Bring Me The Horizon/Sempiternal/01 Can You Feel My Heart.mp3",
                            "playCount": 10,
                            "created": "2025-12-30T00:30:33Z",
                            "albumId": "alb1",
                            "artistId": "art1",
                            "type": "music",
                            "isVideo": false
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetAlbumParser.parse(JSONObject(jsonString))

        TestFixtures.assertNavidromeMetadata(response)

        val album = response.album
        assertEquals("alb1", album.id)
        assertEquals("Sempiternal", album.name)
        assertEquals(6, album.playCount)
        assertEquals(5, album.userRating)
        assertEquals(1, album.songs.size)

        val song = album.songs[0]
        assertEquals("song1", song.id)
        assertEquals("Can You Feel My Heart", song.title)
        assertEquals(false, song.isDir)
        assertEquals(227, song.duration)
        assertEquals(10, song.playCount)
        assertEquals("audio/mpeg", song.contentType)
        assertEquals("music", song.type)
    }

    @Test
    fun `parse handles single song object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "album": {
                    "id": "alb1",
                    "name": "Sempiternal",
                    "song": {
                        "id": "song1",
                        "title": "Can You Feel My Heart",
                        "type": "music"
                    }
                }
            }
        """.trimIndent()

        val response = GetAlbumParser.parse(JSONObject(jsonString))

        assertEquals(1, response.album.songs.size)
        assertEquals("song1", response.album.songs[0].id)
        assertEquals("music", response.album.songs[0].type)
    }

    @Test
    fun `parse handles album with no songs`() {
        val jsonString = """
            {
                "status": "ok",
                "album": {
                    "id": "alb2",
                    "name": "Empty Album",
                    "song": []
                }
            }
        """.trimIndent()

        val response = GetAlbumParser.parse(JSONObject(jsonString))

        assertEquals("alb2", response.album.id)
        assertTrue(response.album.songs.isEmpty())
    }

    @Test
    fun `parse captures starred timestamp`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "album": {
                    "id": "180",
                    "name": "Collapse Into Now",
                    "artist": "R.E.M.",
                    "artistId": "144",
                    "songCount": 12,
                    "duration": 2459,
                    "created": "2011-03-23T09:37:55",
                    "starred": "2012-04-05T19:02:02"
                }
            }
        """.trimIndent()

        val response = GetAlbumParser.parse(JSONObject(jsonString))

        assertEquals("2012-04-05T19:02:02", response.album.starred)
    }

    @Test
    fun `parse starred is null when absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "album": {
                    "id": "178",
                    "name": "Postcards From A Young Man"
                }
            }
        """.trimIndent()

        val response = GetAlbumParser.parse(JSONObject(jsonString))

        assertNull(response.album.starred)
    }

    @Test
    fun `parse returns default album when album key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetAlbumParser.parse(JSONObject(jsonString))

        assertEquals("", response.album.id)
        assertEquals("", response.album.name)
        assertTrue(response.album.songs.isEmpty())
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetAlbumParser.parse(
            TestFixtures.navidromeResponseJson("album", """{"id": "x", "name": "X", "song": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
