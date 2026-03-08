package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetPlaylistsParserTest {

    @Test
    fun `parse returns playlists from playlists container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "playlists": {
                    "playlist": [
                        {
                            "id": "15",
                            "name": "Some random songs",
                            "comment": "Just something I tossed together",
                            "owner": "admin",
                            "public": false,
                            "songCount": 6,
                            "duration": 1391,
                            "created": "2012-04-17T19:53:44",
                            "coverArt": "pl-15",
                            "allowedUser": ["sindre", "john"]
                        },
                        {
                            "id": "16",
                            "name": "More random songs",
                            "comment": "No comment",
                            "owner": "admin",
                            "public": true,
                            "songCount": 5,
                            "duration": 1018,
                            "created": "2012-04-17T19:55:49",
                            "coverArt": "pl-16"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetPlaylistsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.11.0", response.apiVersion)
        assertEquals(2, response.playlists.size)

        val first = response.playlists[0]
        assertEquals("15", first.id)
        assertEquals("Some random songs", first.name)
        assertEquals("Just something I tossed together", first.comment)
        assertEquals("admin", first.owner)
        assertFalse(first.public)
        assertEquals(6, first.songCount)
        assertEquals(1391, first.duration)
        assertEquals("2012-04-17T19:53:44", first.created)
        assertEquals("pl-15", first.coverArt)
        assertEquals(listOf("sindre", "john"), first.allowedUsers)

        val second = response.playlists[1]
        assertEquals("16", second.id)
        assertEquals("More random songs", second.name)
        assertEquals("No comment", second.comment)
        assertTrue(second.public)
        assertEquals(5, second.songCount)
        assertEquals(1018, second.duration)
        assertEquals("pl-16", second.coverArt)
        assertEquals(emptyList(), second.allowedUsers)
    }

    @Test
    fun `parse handles single allowedUser string instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "playlists": {
                    "playlist": [
                        {
                            "id": "15",
                            "name": "Some random songs",
                            "owner": "admin",
                            "public": false,
                            "songCount": 3,
                            "duration": 600,
                            "allowedUser": "sindre"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetPlaylistsParser.parse(JSONObject(jsonString))

        assertEquals(listOf("sindre"), response.playlists[0].allowedUsers)
    }

    @Test
    fun `parse handles single playlist object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "playlists": {
                    "playlist": {
                        "id": "15",
                        "name": "Some random songs",
                        "owner": "admin",
                        "public": false,
                        "songCount": 3,
                        "duration": 600
                    }
                }
            }
        """.trimIndent()

        val response = GetPlaylistsParser.parse(JSONObject(jsonString))

        assertEquals(1, response.playlists.size)
        assertEquals("15", response.playlists[0].id)
        assertEquals("Some random songs", response.playlists[0].name)
    }

    @Test
    fun `parse returns empty list when playlists container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0"
            }
        """.trimIndent()

        val response = GetPlaylistsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.playlists.size)
    }

    @Test
    fun `parse returns empty list when playlist array is absent inside container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "playlists": {}
            }
        """.trimIndent()

        val response = GetPlaylistsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.playlists.size)
    }

    @Test
    fun `parse handles optional fields as null when absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "playlists": {
                    "playlist": [
                        {
                            "id": "15",
                            "name": "Minimal playlist",
                            "public": false,
                            "songCount": 0,
                            "duration": 0
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetPlaylistsParser.parse(JSONObject(jsonString))

        val playlist = response.playlists[0]
        assertNull(playlist.comment)
        assertNull(playlist.owner)
        assertNull(playlist.created)
        assertNull(playlist.coverArt)
        assertEquals(emptyList(), playlist.allowedUsers)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetPlaylistsParser.parse(
            TestFixtures.navidromeResponseJson("playlists", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
