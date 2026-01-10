package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class GetSongsByGenreParserTest {

    @Test
    fun `parse returns songs list from songsByGenre array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "songsByGenre": {
                    "song": [
                        {
                            "id": "16",
                            "parent": "15",
                            "title": "Atrapado",
                            "album": "Antígona",
                            "artist": "Antígona",
                            "isDir": false,
                            "coverArt": "15",
                            "created": "2012-12-26T17:05:54",
                            "duration": 261,
                            "bitRate": 128,
                            "track": 1,
                            "year": 2008,
                            "genre": "Metal",
                            "size": 4188288,
                            "suffix": "mp3",
                            "contentType": "audio/mpeg",
                            "isVideo": false,
                            "path": "Antigona/Antigona/01 - Atrapado.mp3",
                            "albumId": "2",
                            "artistId": "2",
                            "type": "music"
                        },
                        {
                            "id": "17",
                            "parent": "15",
                            "title": "Gritar al cielo",
                            "album": "Antígona",
                            "artist": "Antígona",
                            "isDir": false,
                            "coverArt": "15",
                            "created": "2012-12-26T17:05:44",
                            "duration": 233,
                            "bitRate": 128,
                            "track": 2,
                            "year": 2008,
                            "genre": "Metal",
                            "size": 3737728,
                            "suffix": "mp3",
                            "contentType": "audio/mpeg",
                            "isVideo": false,
                            "path": "Antigona/Antigona/02 - Gritar al cielo.mp3",
                            "albumId": "2",
                            "artistId": "2",
                            "type": "music"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetSongsByGenreParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.9.0", response.apiVersion)
        assertEquals(2, response.songs.size)

        val first = response.songs[0]
        assertEquals("16", first.id)
        assertEquals("15", first.parent)
        assertEquals("Atrapado", first.title)
        assertEquals("Antígona", first.album)
        assertEquals("Antígona", first.artist)
        assertEquals(false, first.isDir)
        assertEquals("15", first.coverArt)
        assertEquals("2012-12-26T17:05:54", first.created)
        assertEquals(261, first.duration)
        assertEquals(128, first.bitRate)
        assertEquals(1, first.track)
        assertEquals(2008, first.year)
        assertEquals("Metal", first.genre)
        assertEquals(4188288L, first.size)
        assertEquals("mp3", first.suffix)
        assertEquals("audio/mpeg", first.contentType)
        assertEquals(false, first.isVideo)
        assertEquals("Antigona/Antigona/01 - Atrapado.mp3", first.path)
        assertEquals("2", first.albumId)
        assertEquals("2", first.artistId)
        assertEquals("music", first.type)

        val second = response.songs[1]
        assertEquals("17", second.id)
        assertEquals("Gritar al cielo", second.title)
        assertEquals(2, second.track)
    }

    @Test
    fun `parse returns empty list when songsByGenre container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0"
            }
        """.trimIndent()

        val response = GetSongsByGenreParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse returns empty list when song array is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "songsByGenre": {}
            }
        """.trimIndent()

        val response = GetSongsByGenreParser.parse(JSONObject(jsonString))

        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse handles single song object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "songsByGenre": {
                    "song": {
                        "id": "18",
                        "parent": "15",
                        "title": "En sus garras",
                        "album": "Antígona",
                        "artist": "Antígona",
                        "isDir": false,
                        "coverArt": "15",
                        "created": "2012-12-26T17:05:22",
                        "duration": 239,
                        "bitRate": 128,
                        "track": 3,
                        "year": 2008,
                        "genre": "Metal",
                        "size": 3846272,
                        "suffix": "mp3",
                        "contentType": "audio/mpeg",
                        "isVideo": false,
                        "path": "Antigona/Antigona/03 - En sus garras.mp3",
                        "albumId": "2",
                        "artistId": "2",
                        "type": "music"
                    }
                }
            }
        """.trimIndent()

        val response = GetSongsByGenreParser.parse(JSONObject(jsonString))

        assertEquals(1, response.songs.size)
        assertEquals("18", response.songs[0].id)
        assertEquals("En sus garras", response.songs[0].title)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetSongsByGenreParser.parse(
            TestFixtures.navidromeResponseJson("songsByGenre", """{"song": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(0, response.songs.size)
    }
}
