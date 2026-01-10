package com.vsulimov.libsubsonic.parser.search

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class SearchParserTest {

    @Test
    fun `parse returns artists albums and songs from searchResult3 container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "searchResult3": {
                    "artist": [
                        {
                            "id": "5944",
                            "name": "Black",
                            "coverArt": "ar-5944",
                            "albumCount": 2
                        },
                        {
                            "id": "5785",
                            "name": "Black Sabbath",
                            "coverArt": "ar-5785",
                            "albumCount": 22
                        }
                    ],
                    "album": [
                        {
                            "id": "11241",
                            "name": "Black",
                            "coverArt": "al-11241",
                            "songCount": 10,
                            "created": "2004-11-14T13:02:03",
                            "duration": 2575,
                            "artist": "Black",
                            "artistId": "5944"
                        }
                    ],
                    "song": [
                        {
                            "id": "77451",
                            "parent": "77433",
                            "title": "Black",
                            "album": "Angry Machines",
                            "artist": "Dio",
                            "isDir": false,
                            "coverArt": "77433",
                            "duration": 190,
                            "bitRate": 192,
                            "track": 3,
                            "year": 1996,
                            "genre": "Hard Rock",
                            "size": 4575589,
                            "suffix": "mp3",
                            "contentType": "audio/mpeg",
                            "isVideo": false,
                            "path": "Dio/Angry Machines/Angry Machines 2.mp3",
                            "albumId": "12168",
                            "artistId": "6357",
                            "type": "music"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = SearchParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.8.0", response.apiVersion)

        assertEquals(2, response.artists.size)
        val firstArtist = response.artists[0]
        assertEquals("5944", firstArtist.id)
        assertEquals("Black", firstArtist.name)
        assertEquals("ar-5944", firstArtist.coverArt)
        assertEquals(2, firstArtist.albumCount)

        val secondArtist = response.artists[1]
        assertEquals("5785", secondArtist.id)
        assertEquals("Black Sabbath", secondArtist.name)
        assertEquals(22, secondArtist.albumCount)

        assertEquals(1, response.albums.size)
        val album = response.albums[0]
        assertEquals("11241", album.id)
        assertEquals("Black", album.name)
        assertEquals("al-11241", album.coverArt)
        assertEquals(10, album.songCount)
        assertEquals("2004-11-14T13:02:03", album.created)
        assertEquals(2575, album.duration)
        assertEquals("Black", album.artist)
        assertEquals("5944", album.artistId)

        assertEquals(1, response.songs.size)
        val song = response.songs[0]
        assertEquals("77451", song.id)
        assertEquals("Black", song.title)
        assertEquals("Dio", song.artist)
        assertEquals(190, song.duration)
        assertEquals(192, song.bitRate)
        assertEquals(3, song.track)
        assertEquals(1996, song.year)
        assertEquals("Hard Rock", song.genre)
        assertEquals(4575589L, song.size)
        assertEquals("mp3", song.suffix)
        assertEquals("audio/mpeg", song.contentType)
        assertEquals("music", song.type)
        assertEquals("Dio/Angry Machines/Angry Machines 2.mp3", song.path)
    }

    @Test
    fun `parse returns empty lists when searchResult3 container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0"
            }
        """.trimIndent()

        val response = SearchParser.parse(JSONObject(jsonString))

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
                "searchResult3": {}
            }
        """.trimIndent()

        val response = SearchParser.parse(JSONObject(jsonString))

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
                "searchResult3": {
                    "artist": {
                        "id": "5944",
                        "name": "Black",
                        "albumCount": 2
                    }
                }
            }
        """.trimIndent()

        val response = SearchParser.parse(JSONObject(jsonString))

        assertEquals(1, response.artists.size)
        assertEquals("5944", response.artists[0].id)
        assertEquals("Black", response.artists[0].name)
        assertNull(response.artists[0].coverArt)
    }

    @Test
    fun `parse handles single album object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "searchResult3": {
                    "album": {
                        "id": "11241",
                        "name": "Black",
                        "artist": "Black",
                        "artistId": "5944"
                    }
                }
            }
        """.trimIndent()

        val response = SearchParser.parse(JSONObject(jsonString))

        assertEquals(1, response.albums.size)
        assertEquals("11241", response.albums[0].id)
        assertEquals("Black", response.albums[0].name)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = SearchParser.parse(
            TestFixtures.navidromeResponseJson("searchResult3", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
