package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class GetTopSongsParserTest {

    @Test
    fun `parse returns songs list from topSongs array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "topSongs": {
                    "song": [
                        {
                            "id": "1013",
                            "parent": "1008",
                            "isDir": false,
                            "title": "London Leatherboys",
                            "album": "Balls To The Wall",
                            "artist": "Accept",
                            "track": 2,
                            "year": 1984,
                            "genre": "Metal",
                            "coverArt": "1008",
                            "size": 5706432,
                            "contentType": "audio/mpeg",
                            "suffix": "mp3",
                            "duration": 237,
                            "bitRate": 192,
                            "path": "Accept/Balls To The Wall/Accept - London Leatherboys - 02.mp3",
                            "isVideo": false,
                            "created": "2004-11-27T17:22:37.000Z",
                            "albumId": "411",
                            "artistId": "278",
                            "type": "music"
                        },
                        {
                            "id": "1012",
                            "parent": "1008",
                            "isDir": false,
                            "title": "Head Over Heels",
                            "album": "Balls To The Wall",
                            "artist": "Accept",
                            "track": 4,
                            "year": 1984,
                            "genre": "Metal",
                            "coverArt": "1008",
                            "size": 6219527,
                            "contentType": "audio/mpeg",
                            "suffix": "mp3",
                            "duration": 259,
                            "bitRate": 192,
                            "path": "Accept/Balls To The Wall/Accept - Head Over Heels - 04.mp3",
                            "isVideo": false,
                            "created": "2004-11-27T17:22:35.000Z",
                            "starred": "2015-01-03T21:12:10.070Z",
                            "albumId": "411",
                            "artistId": "278",
                            "type": "music"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetTopSongsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.13.0", response.apiVersion)
        assertEquals(2, response.songs.size)

        val first = response.songs[0]
        assertEquals("1013", first.id)
        assertEquals("1008", first.parent)
        assertEquals(false, first.isDir)
        assertEquals("London Leatherboys", first.title)
        assertEquals("Balls To The Wall", first.album)
        assertEquals("Accept", first.artist)
        assertEquals(2, first.track)
        assertEquals(1984, first.year)
        assertEquals("Metal", first.genre)
        assertEquals("1008", first.coverArt)
        assertEquals(5706432L, first.size)
        assertEquals("audio/mpeg", first.contentType)
        assertEquals("mp3", first.suffix)
        assertEquals(237, first.duration)
        assertEquals(192, first.bitRate)
        assertEquals("Accept/Balls To The Wall/Accept - London Leatherboys - 02.mp3", first.path)
        assertEquals(false, first.isVideo)
        assertEquals("2004-11-27T17:22:37.000Z", first.created)
        assertEquals("411", first.albumId)
        assertEquals("278", first.artistId)
        assertEquals("music", first.type)

        val second = response.songs[1]
        assertEquals("1012", second.id)
        assertEquals("Head Over Heels", second.title)
        assertEquals(259, second.duration)
    }

    @Test
    fun `parse returns empty list when topSongs container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0"
            }
        """.trimIndent()

        val response = GetTopSongsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse returns empty list when song array is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "topSongs": {}
            }
        """.trimIndent()

        val response = GetTopSongsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse handles single song object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "topSongs": {
                    "song": {
                        "id": "1016",
                        "parent": "1008",
                        "isDir": false,
                        "title": "Love Child",
                        "album": "Balls To The Wall",
                        "artist": "Accept",
                        "track": 6,
                        "year": 1984,
                        "genre": "Metal",
                        "coverArt": "1008",
                        "size": 5156143,
                        "contentType": "audio/mpeg",
                        "suffix": "mp3",
                        "duration": 214,
                        "bitRate": 192,
                        "path": "Accept/Balls To The Wall/Accept - Love Child - 06.mp3",
                        "isVideo": false,
                        "created": "2004-11-27T17:22:42.000Z",
                        "albumId": "411",
                        "artistId": "278",
                        "type": "music"
                    }
                }
            }
        """.trimIndent()

        val response = GetTopSongsParser.parse(JSONObject(jsonString))

        assertEquals(1, response.songs.size)
        assertEquals("1016", response.songs[0].id)
        assertEquals("Love Child", response.songs[0].title)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetTopSongsParser.parse(
            TestFixtures.navidromeResponseJson("topSongs", """{"song": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(0, response.songs.size)
    }
}
