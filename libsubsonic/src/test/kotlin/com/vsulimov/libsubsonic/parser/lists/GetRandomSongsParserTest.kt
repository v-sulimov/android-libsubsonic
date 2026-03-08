package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetRandomSongsParserTest {

    @Test
    fun `parse returns songs list from randomSongs array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.4.0",
                "randomSongs": {
                    "song": [
                        {
                            "id": "111",
                            "parent": "11",
                            "title": "Dancing Queen",
                            "isDir": false,
                            "album": "Arrival",
                            "artist": "ABBA",
                            "track": 7,
                            "year": 1978,
                            "genre": "Pop",
                            "coverArt": "24",
                            "size": 8421341,
                            "contentType": "audio/mpeg",
                            "suffix": "mp3",
                            "duration": 146,
                            "bitRate": 128,
                            "path": "ABBA/Arrival/Dancing Queen.mp3"
                        },
                        {
                            "id": "112",
                            "parent": "11",
                            "title": "Money, Money, Money",
                            "isDir": false,
                            "album": "Arrival",
                            "artist": "ABBA",
                            "track": 7,
                            "year": 1978,
                            "genre": "Pop",
                            "coverArt": "25",
                            "size": 4910028,
                            "contentType": "audio/flac",
                            "suffix": "flac",
                            "transcodedContentType": "audio/mpeg",
                            "transcodedSuffix": "mp3",
                            "duration": 208,
                            "bitRate": 128,
                            "path": "ABBA/Arrival/Money, Money, Money.mp3"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetRandomSongsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.4.0", response.apiVersion)
        assertEquals(2, response.songs.size)

        val first = response.songs[0]
        assertEquals("111", first.id)
        assertEquals("11", first.parent)
        assertEquals("Dancing Queen", first.title)
        assertEquals(false, first.isDir)
        assertEquals("Arrival", first.album)
        assertEquals("ABBA", first.artist)
        assertEquals(7, first.track)
        assertEquals(1978, first.year)
        assertEquals("Pop", first.genre)
        assertEquals("24", first.coverArt)
        assertEquals(8421341L, first.size)
        assertEquals("audio/mpeg", first.contentType)
        assertEquals("mp3", first.suffix)
        assertEquals(146, first.duration)
        assertEquals(128, first.bitRate)
        assertEquals("ABBA/Arrival/Dancing Queen.mp3", first.path)
        assertNull(first.transcodedContentType)
        assertNull(first.transcodedSuffix)

        val second = response.songs[1]
        assertEquals("112", second.id)
        assertEquals("Money, Money, Money", second.title)
        assertEquals("audio/flac", second.contentType)
        assertEquals("flac", second.suffix)
        assertEquals("audio/mpeg", second.transcodedContentType)
        assertEquals("mp3", second.transcodedSuffix)
    }

    @Test
    fun `parse returns empty list when randomSongs container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.4.0"
            }
        """.trimIndent()

        val response = GetRandomSongsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse returns empty list when song array is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.4.0",
                "randomSongs": {}
            }
        """.trimIndent()

        val response = GetRandomSongsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse handles single song object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.4.0",
                "randomSongs": {
                    "song": {
                        "id": "111",
                        "parent": "11",
                        "title": "Dancing Queen",
                        "isDir": false,
                        "album": "Arrival",
                        "artist": "ABBA",
                        "track": 7,
                        "year": 1978,
                        "genre": "Pop",
                        "coverArt": "24",
                        "size": 8421341,
                        "contentType": "audio/mpeg",
                        "suffix": "mp3",
                        "duration": 146,
                        "bitRate": 128,
                        "path": "ABBA/Arrival/Dancing Queen.mp3"
                    }
                }
            }
        """.trimIndent()

        val response = GetRandomSongsParser.parse(JSONObject(jsonString))

        assertEquals(1, response.songs.size)
        assertEquals("111", response.songs[0].id)
        assertEquals("Dancing Queen", response.songs[0].title)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetRandomSongsParser.parse(
            TestFixtures.navidromeResponseJson("randomSongs", """{"song": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(0, response.songs.size)
    }
}
