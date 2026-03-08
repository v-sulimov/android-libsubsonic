package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetUsersParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0"
            }
        """.trimIndent()

        val response = GetUsersParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.12.0", response.apiVersion)
    }

    @Test
    fun `parse returns empty list when users is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0"
            }
        """.trimIndent()

        val response = GetUsersParser.parse(JSONObject(jsonString))

        assertEquals(emptyList(), response.users)
    }

    @Test
    fun `parse extracts users with all fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0",
                "users": {
                    "user": [
                        {
                            "username": "admin",
                            "scrobblingEnabled": true,
                            "adminRole": true,
                            "settingsRole": true,
                            "downloadRole": true,
                            "uploadRole": true,
                            "playlistRole": true,
                            "coverArtRole": true,
                            "commentRole": true,
                            "podcastRole": true,
                            "streamRole": true,
                            "jukeboxRole": true,
                            "shareRole": true,
                            "folder": [1, 2, 3]
                        },
                        {
                            "username": "sindre",
                            "email": "sindre@activeobjects.no",
                            "scrobblingEnabled": false,
                            "adminRole": false,
                            "settingsRole": true,
                            "downloadRole": false,
                            "uploadRole": false,
                            "playlistRole": true,
                            "coverArtRole": false,
                            "commentRole": false,
                            "podcastRole": false,
                            "streamRole": true,
                            "jukeboxRole": false,
                            "shareRole": false,
                            "folder": [1, 3]
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetUsersParser.parse(JSONObject(jsonString))

        assertEquals(2, response.users.size)
        assertEquals("admin", response.users[0].username)
        assertNull(response.users[0].email)
        assertEquals(listOf(1, 2, 3), response.users[0].folders)
        assertEquals("sindre", response.users[1].username)
        assertEquals("sindre@activeobjects.no", response.users[1].email)
        assertEquals(listOf(1, 3), response.users[1].folders)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetUsersParser.parse(
            TestFixtures.navidromeResponseJson("users", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
