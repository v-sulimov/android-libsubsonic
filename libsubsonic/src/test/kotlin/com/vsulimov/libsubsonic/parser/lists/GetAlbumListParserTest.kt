package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetAlbumListParserTest {

    @Test
    fun `parse returns albums list from albumList2 array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "albumList2": {
                    "album": [
                        {
                            "id": "1768",
                            "name": "Duets",
                            "coverArt": "al-1768",
                            "songCount": 2,
                            "created": "2002-11-09T15:44:40",
                            "duration": 514,
                            "artist": "Nik Kershaw",
                            "artistId": "829"
                        },
                        {
                            "id": "4414",
                            "name": "For Sale",
                            "songCount": 14,
                            "created": "2007-10-30T00:11:58",
                            "duration": 2046,
                            "artist": "The Beatles",
                            "artistId": "509"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetAlbumListParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.8.0", response.apiVersion)
        assertEquals(2, response.albums.size)

        val first = response.albums[0]
        assertEquals("1768", first.id)
        assertEquals("Duets", first.name)
        assertEquals("al-1768", first.coverArt)
        assertEquals(2, first.songCount)
        assertEquals("2002-11-09T15:44:40", first.created)
        assertEquals(514, first.duration)
        assertEquals("Nik Kershaw", first.artist)
        assertEquals("829", first.artistId)
        assertEquals(emptyList(), first.songs)

        val second = response.albums[1]
        assertEquals("4414", second.id)
        assertEquals("For Sale", second.name)
        assertNull(second.coverArt)
        assertEquals("The Beatles", second.artist)
        assertEquals("509", second.artistId)
    }

    @Test
    fun `parse returns empty list when albumList2 container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0"
            }
        """.trimIndent()

        val response = GetAlbumListParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals(0, response.albums.size)
    }

    @Test
    fun `parse returns empty list when album array is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "albumList2": {}
            }
        """.trimIndent()

        val response = GetAlbumListParser.parse(JSONObject(jsonString))

        assertEquals(0, response.albums.size)
    }

    @Test
    fun `parse handles single album object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "albumList2": {
                    "album": {
                        "id": "2277",
                        "name": "Hot",
                        "coverArt": "al-2277",
                        "songCount": 4,
                        "created": "2004-11-28T00:06:52",
                        "duration": 1110,
                        "artist": "Melanie B",
                        "artistId": "1242"
                    }
                }
            }
        """.trimIndent()

        val response = GetAlbumListParser.parse(JSONObject(jsonString))

        assertEquals(1, response.albums.size)
        assertEquals("2277", response.albums[0].id)
        assertEquals("Hot", response.albums[0].name)
        assertEquals("Melanie B", response.albums[0].artist)
    }

    @Test
    fun `parse captures optional album fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "albumList2": {
                    "album": [
                        {
                            "id": "100",
                            "name": "Greatest Hits",
                            "artist": "Various",
                            "artistId": "1",
                            "songCount": 20,
                            "duration": 4800,
                            "created": "2010-01-01T00:00:00",
                            "year": 2010,
                            "genre": "Pop",
                            "playCount": 42,
                            "isCompilation": true
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetAlbumListParser.parse(JSONObject(jsonString))

        val album = response.albums[0]
        assertEquals(2010, album.year)
        assertEquals("Pop", album.genre)
        assertEquals(42, album.playCount)
        assertEquals(true, album.isCompilation)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetAlbumListParser.parse(
            TestFixtures.navidromeResponseJson("albumList2", """{"album": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(0, response.albums.size)
    }
}
