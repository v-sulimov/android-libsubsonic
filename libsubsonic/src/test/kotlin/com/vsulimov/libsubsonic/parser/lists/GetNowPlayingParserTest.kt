package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetNowPlayingParserTest {

    @Test
    fun `parse returns entries list from nowPlaying array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.4.0",
                "nowPlaying": {
                    "entry": [
                        {
                            "username": "sindre",
                            "minutesAgo": 12,
                            "playerId": 2,
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
                            "path": "ABBA/Arrival/Dancing Queen.mp3"
                        },
                        {
                            "username": "bente",
                            "minutesAgo": 1,
                            "playerId": 4,
                            "playerName": "Kitchen",
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
                            "path": "ABBA/Arrival/Money, Money, Money.mp3"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetNowPlayingParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.4.0", response.apiVersion)
        assertEquals(2, response.entries.size)

        val first = response.entries[0]
        assertEquals("sindre", first.username)
        assertEquals(12, first.minutesAgo)
        assertEquals(2, first.playerId)
        assertNull(first.playerName)
        assertEquals("111", first.song.id)
        assertEquals("Dancing Queen", first.song.title)
        assertEquals("ABBA", first.song.artist)
        assertEquals("Arrival", first.song.album)
        assertEquals(1978, first.song.year)
        assertEquals("Pop", first.song.genre)
        assertEquals(8421341L, first.song.size)
        assertEquals("audio/mpeg", first.song.contentType)
        assertNull(first.song.transcodedContentType)
        assertNull(first.song.transcodedSuffix)

        val second = response.entries[1]
        assertEquals("bente", second.username)
        assertEquals(1, second.minutesAgo)
        assertEquals(4, second.playerId)
        assertEquals("Kitchen", second.playerName)
        assertEquals("112", second.song.id)
        assertEquals("Money, Money, Money", second.song.title)
        assertEquals("audio/flac", second.song.contentType)
        assertEquals("audio/mpeg", second.song.transcodedContentType)
        assertEquals("mp3", second.song.transcodedSuffix)
    }

    @Test
    fun `parse returns empty list when nowPlaying container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.4.0"
            }
        """.trimIndent()

        val response = GetNowPlayingParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals(0, response.entries.size)
    }

    @Test
    fun `parse returns empty list when entry array is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.4.0",
                "nowPlaying": {}
            }
        """.trimIndent()

        val response = GetNowPlayingParser.parse(JSONObject(jsonString))

        assertEquals(0, response.entries.size)
    }

    @Test
    fun `parse handles single entry object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.4.0",
                "nowPlaying": {
                    "entry": {
                        "username": "sindre",
                        "minutesAgo": 5,
                        "playerId": 2,
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
                        "path": "ABBA/Arrival/Dancing Queen.mp3"
                    }
                }
            }
        """.trimIndent()

        val response = GetNowPlayingParser.parse(JSONObject(jsonString))

        assertEquals(1, response.entries.size)
        assertEquals("sindre", response.entries[0].username)
        assertEquals("Dancing Queen", response.entries[0].song.title)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetNowPlayingParser.parse(
            TestFixtures.navidromeResponseJson("nowPlaying", """{"entry": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(0, response.entries.size)
    }
}
