package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.json.JSONObject

class CreatePlaylistParserTest {

    @Test
    fun `parse extracts newly created playlist header and song entries`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "playlist": {
                    "id": "42",
                    "name": "My New Playlist",
                    "comment": "fresh",
                    "owner": "admin",
                    "public": true,
                    "songCount": 2,
                    "duration": 340,
                    "created": "2024-01-10T12:00:00",
                    "coverArt": "pl-42",
                    "allowedUser": ["alice", "bob"],
                    "entry": [
                        {
                            "id": "101",
                            "parent": "100",
                            "title": "First Song",
                            "album": "Some Album",
                            "artist": "Some Artist",
                            "isDir": false,
                            "duration": 180,
                            "bitRate": 320,
                            "track": 1,
                            "year": 2020,
                            "type": "music"
                        },
                        {
                            "id": "102",
                            "parent": "100",
                            "title": "Second Song",
                            "album": "Some Album",
                            "artist": "Some Artist",
                            "isDir": false,
                            "duration": 160,
                            "bitRate": 256,
                            "track": 2,
                            "year": 2020,
                            "genre": "Rock",
                            "type": "music"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = CreatePlaylistParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.16.1", response.apiVersion)

        val playlist = response.playlist
        assertEquals("42", playlist.id)
        assertEquals("My New Playlist", playlist.name)
        assertEquals("fresh", playlist.comment)
        assertEquals("admin", playlist.owner)
        assertTrue(playlist.public)
        assertEquals(2, playlist.songCount)
        assertEquals(340, playlist.duration)
        assertEquals("2024-01-10T12:00:00", playlist.created)
        assertEquals("pl-42", playlist.coverArt)
        assertEquals(listOf("alice", "bob"), playlist.allowedUsers)

        assertEquals(2, playlist.entries.size)
        val first = playlist.entries[0]
        assertEquals("101", first.id)
        assertEquals("First Song", first.title)
        assertEquals("Some Artist", first.artist)
        assertEquals(180, first.duration)
        assertEquals(320, first.bitRate)
        assertEquals(1, first.track)
        assertEquals(2020, first.year)
        assertEquals("music", first.type)

        val second = playlist.entries[1]
        assertEquals("102", second.id)
        assertEquals("Second Song", second.title)
        assertEquals("Rock", second.genre)
    }

    @Test
    fun `parse handles single entry object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "playlist": {
                    "id": "42",
                    "name": "Single Song Playlist",
                    "public": false,
                    "songCount": 1,
                    "duration": 180,
                    "entry": {
                        "id": "101",
                        "title": "Only Song",
                        "isDir": false,
                        "type": "music"
                    }
                }
            }
        """.trimIndent()

        val response = CreatePlaylistParser.parse(JSONObject(jsonString))

        assertEquals(1, response.playlist.entries.size)
        assertEquals("101", response.playlist.entries[0].id)
        assertEquals("Only Song", response.playlist.entries[0].title)
    }

    @Test
    fun `parse returns empty entries list when no songs were added`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "playlist": {
                    "id": "42",
                    "name": "Empty Playlist",
                    "public": false,
                    "songCount": 0,
                    "duration": 0
                }
            }
        """.trimIndent()

        val response = CreatePlaylistParser.parse(JSONObject(jsonString))

        assertEquals("42", response.playlist.id)
        assertEquals("Empty Playlist", response.playlist.name)
        assertFalse(response.playlist.public)
        assertEquals(0, response.playlist.entries.size)
    }

    @Test
    fun `parse returns default playlist when playlist object is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = CreatePlaylistParser.parse(JSONObject(jsonString))

        assertEquals("", response.playlist.id)
        assertEquals("", response.playlist.name)
        assertEquals(0, response.playlist.entries.size)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = CreatePlaylistParser.parse(
            TestFixtures.navidromeResponseJson(
                "playlist",
                """{"id": "42", "name": "My Playlist", "public": false, "songCount": 0, "duration": 0}"""
            )
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
