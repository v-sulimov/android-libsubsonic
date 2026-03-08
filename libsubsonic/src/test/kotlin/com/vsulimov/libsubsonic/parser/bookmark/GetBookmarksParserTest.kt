package com.vsulimov.libsubsonic.parser.bookmark

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetBookmarksParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0"
            }
        """.trimIndent()

        val response = GetBookmarksParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.9.0", response.apiVersion)
    }

    @Test
    fun `parse returns empty list when bookmarks is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0"
            }
        """.trimIndent()

        val response = GetBookmarksParser.parse(JSONObject(jsonString))

        assertEquals(emptyList(), response.bookmarks)
    }

    @Test
    fun `parse extracts bookmark fields and entry`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "bookmarks": {
                    "bookmark": [
                        {
                            "position": 60300,
                            "username": "admin",
                            "comment": "Listened this far",
                            "created": "2013-04-12T13:45:36",
                            "changed": "2013-04-12T13:45:36",
                            "entry": {
                                "id": "2804",
                                "parent": "2655",
                                "title": "Episode 10",
                                "album": "Hitchikers Guide To The Galaxy",
                                "artist": "BBC Radio",
                                "isDir": false,
                                "created": "2003-11-25T22:02:08",
                                "duration": 1772,
                                "bitRate": 96,
                                "size": 21240443,
                                "suffix": "mp3",
                                "contentType": "audio/mpeg",
                                "isVideo": false,
                                "path": "Audio Books/Hitchhiker's Guide to the Galaxy/Episode 10.mp3",
                                "albumId": "514",
                                "artistId": "334",
                                "type": "audiobook"
                            }
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetBookmarksParser.parse(JSONObject(jsonString))

        assertEquals(1, response.bookmarks.size)
        val bookmark = response.bookmarks[0]
        assertEquals(60300L, bookmark.position)
        assertEquals("admin", bookmark.username)
        assertEquals("Listened this far", bookmark.comment)
        assertEquals("2013-04-12T13:45:36", bookmark.created)
        assertEquals("2013-04-12T13:45:36", bookmark.changed)
        assertEquals("2804", bookmark.entry.id)
        assertEquals("Episode 10", bookmark.entry.title)
    }

    @Test
    fun `parse sets comment to null when absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "bookmarks": {
                    "bookmark": [
                        {
                            "position": 0,
                            "username": "admin",
                            "created": "2013-04-12T13:45:36",
                            "changed": "2013-04-12T13:45:36",
                            "entry": {
                                "id": "2804",
                                "isDir": false,
                                "title": "Episode 10",
                                "isVideo": false
                            }
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetBookmarksParser.parse(JSONObject(jsonString))

        assertNull(response.bookmarks[0].comment)
    }

    @Test
    fun `parse handles single bookmark object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "bookmarks": {
                    "bookmark": {
                        "position": 12345,
                        "username": "admin",
                        "created": "2013-04-12T13:45:36",
                        "changed": "2013-04-12T13:45:36",
                        "entry": {
                            "id": "100",
                            "isDir": false,
                            "title": "Single Entry",
                            "isVideo": false
                        }
                    }
                }
            }
        """.trimIndent()

        val response = GetBookmarksParser.parse(JSONObject(jsonString))

        assertEquals(1, response.bookmarks.size)
        assertEquals(12345L, response.bookmarks[0].position)
        assertEquals("100", response.bookmarks[0].entry.id)
    }

    @Test
    fun `parse returns empty list when bookmarks container is empty`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "bookmarks": {}
            }
        """.trimIndent()

        val response = GetBookmarksParser.parse(JSONObject(jsonString))

        assertEquals(emptyList(), response.bookmarks)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetBookmarksParser.parse(
            TestFixtures.navidromeResponseJson("bookmarks", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
