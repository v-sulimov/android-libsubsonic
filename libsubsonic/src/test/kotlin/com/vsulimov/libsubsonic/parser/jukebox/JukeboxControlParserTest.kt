package com.vsulimov.libsubsonic.parser.jukebox

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class JukeboxControlParserTest {

    @Test
    fun `parse extracts jukeboxStatus fields correctly`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.7.0",
                "jukeboxStatus": {
                    "currentIndex": 7,
                    "playing": true,
                    "gain": 0.9,
                    "position": 67
                }
            }
        """.trimIndent()

        val response = JukeboxControlParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.7.0", response.apiVersion)
        assertEquals(7, response.jukeboxStatus.currentIndex)
        assertTrue(response.jukeboxStatus.playing)
        assertEquals(0.9, response.jukeboxStatus.gain)
        assertEquals(67, response.jukeboxStatus.position)
        assertEquals(emptyList(), response.jukeboxStatus.entries)
    }

    @Test
    fun `parse extracts jukeboxPlaylist with entries correctly`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.4.0",
                "jukeboxPlaylist": {
                    "currentIndex": 0,
                    "playing": true,
                    "gain": 0.67,
                    "position": 67,
                    "entry": [
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
                            "duration": 345,
                            "size": 8421341,
                            "contentType": "audio/mpeg",
                            "suffix": "mp3",
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
                            "duration": 240,
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

        val response = JukeboxControlParser.parse(JSONObject(jsonString))

        assertEquals(0, response.jukeboxStatus.currentIndex)
        assertTrue(response.jukeboxStatus.playing)
        assertEquals(0.67, response.jukeboxStatus.gain)
        assertEquals(67, response.jukeboxStatus.position)
        assertEquals(2, response.jukeboxStatus.entries.size)

        val first = response.jukeboxStatus.entries[0]
        assertEquals("111", first.id)
        assertEquals("Dancing Queen", first.title)
        assertEquals("ABBA", first.artist)
        assertEquals(345, first.duration)

        val second = response.jukeboxStatus.entries[1]
        assertEquals("112", second.id)
        assertEquals("audio/flac", second.contentType)
        assertEquals("audio/mpeg", second.transcodedContentType)
        assertEquals("mp3", second.transcodedSuffix)
    }

    @Test
    fun `parse handles missing optional position field`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.7.0",
                "jukeboxStatus": {
                    "currentIndex": 0,
                    "playing": false,
                    "gain": 0.5
                }
            }
        """.trimIndent()

        val response = JukeboxControlParser.parse(JSONObject(jsonString))

        assertNull(response.jukeboxStatus.position)
    }

    @Test
    fun `parse prefers jukeboxPlaylist over jukeboxStatus when both absent returns defaults`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.7.0"
            }
        """.trimIndent()

        val response = JukeboxControlParser.parse(JSONObject(jsonString))

        assertEquals(0, response.jukeboxStatus.currentIndex)
        assertEquals(0.0, response.jukeboxStatus.gain)
        assertEquals(emptyList(), response.jukeboxStatus.entries)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = JukeboxControlParser.parse(
            TestFixtures.navidromeResponseJson(
                "jukeboxStatus",
                """{"currentIndex": 0, "playing": false, "gain": 0.5}"""
            )
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
