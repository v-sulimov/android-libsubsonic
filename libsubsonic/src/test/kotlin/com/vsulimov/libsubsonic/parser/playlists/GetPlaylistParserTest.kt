package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.json.JSONObject

class GetPlaylistParserTest {

    @Test
    fun `parse extracts playlist header and song entries`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "playlist": {
                    "id": "15",
                    "name": "kokos",
                    "comment": "fan",
                    "owner": "admin",
                    "public": true,
                    "songCount": 6,
                    "duration": 1391,
                    "created": "2012-04-17T19:53:44",
                    "coverArt": "pl-15",
                    "allowedUser": ["sindre", "john"],
                    "entry": [
                        {
                            "id": "657",
                            "parent": "655",
                            "title": "Making Me Nervous",
                            "album": "I Don't Know What I'm Doing",
                            "artist": "Brad Sucks",
                            "isDir": false,
                            "coverArt": "655",
                            "duration": 159,
                            "bitRate": 202,
                            "track": 1,
                            "year": 2003,
                            "size": 4060113,
                            "suffix": "mp3",
                            "contentType": "audio/mpeg",
                            "isVideo": false,
                            "albumId": "58",
                            "artistId": "45",
                            "type": "music"
                        },
                        {
                            "id": "823",
                            "parent": "784",
                            "title": "Piano escena",
                            "album": "BSO Sebastian",
                            "artist": "PeerGynt Lobogris",
                            "isDir": false,
                            "duration": 129,
                            "bitRate": 170,
                            "track": 8,
                            "year": 2008,
                            "genre": "Blues",
                            "type": "music"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetPlaylistParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.11.0", response.apiVersion)

        val playlist = response.playlist
        assertEquals("15", playlist.id)
        assertEquals("kokos", playlist.name)
        assertEquals("fan", playlist.comment)
        assertEquals("admin", playlist.owner)
        assertTrue(playlist.public)
        assertEquals(6, playlist.songCount)
        assertEquals(1391, playlist.duration)
        assertEquals("2012-04-17T19:53:44", playlist.created)
        assertEquals("pl-15", playlist.coverArt)
        assertEquals(listOf("sindre", "john"), playlist.allowedUsers)

        assertEquals(2, playlist.entries.size)
        val first = playlist.entries[0]
        assertEquals("657", first.id)
        assertEquals("Making Me Nervous", first.title)
        assertEquals("Brad Sucks", first.artist)
        assertEquals(159, first.duration)
        assertEquals(202, first.bitRate)
        assertEquals(1, first.track)
        assertEquals(2003, first.year)
        assertEquals("music", first.type)

        val second = playlist.entries[1]
        assertEquals("823", second.id)
        assertEquals("Piano escena", second.title)
        assertEquals("Blues", second.genre)
    }

    @Test
    fun `parse handles single entry object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "playlist": {
                    "id": "15",
                    "name": "kokos",
                    "public": false,
                    "songCount": 1,
                    "duration": 159,
                    "entry": {
                        "id": "657",
                        "title": "Making Me Nervous",
                        "isDir": false,
                        "type": "music"
                    }
                }
            }
        """.trimIndent()

        val response = GetPlaylistParser.parse(JSONObject(jsonString))

        assertEquals(1, response.playlist.entries.size)
        assertEquals("657", response.playlist.entries[0].id)
        assertEquals("Making Me Nervous", response.playlist.entries[0].title)
    }

    @Test
    fun `parse returns empty entries list when entry array is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "playlist": {
                    "id": "15",
                    "name": "Empty playlist",
                    "public": false,
                    "songCount": 0,
                    "duration": 0
                }
            }
        """.trimIndent()

        val response = GetPlaylistParser.parse(JSONObject(jsonString))

        assertEquals("15", response.playlist.id)
        assertEquals(0, response.playlist.entries.size)
        assertFalse(response.playlist.public)
    }

    @Test
    fun `parse returns default playlist when playlist object is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0"
            }
        """.trimIndent()

        val response = GetPlaylistParser.parse(JSONObject(jsonString))

        assertEquals("", response.playlist.id)
        assertEquals("", response.playlist.name)
        assertEquals(0, response.playlist.entries.size)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetPlaylistParser.parse(
            TestFixtures.navidromeResponseJson(
                "playlist",
                """{"id": "15", "name": "kokos", "public": false, "songCount": 0, "duration": 0}"""
            )
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
