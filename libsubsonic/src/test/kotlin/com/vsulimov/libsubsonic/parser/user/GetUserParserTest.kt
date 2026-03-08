package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.json.JSONObject

class GetUserParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0",
                "user": {
                    "username": "sindre",
                    "email": "sindre@activeobjects.no",
                    "scrobblingEnabled": true,
                    "adminRole": false,
                    "settingsRole": true,
                    "downloadRole": true,
                    "uploadRole": false,
                    "playlistRole": true,
                    "coverArtRole": true,
                    "commentRole": true,
                    "podcastRole": true,
                    "streamRole": true,
                    "jukeboxRole": true,
                    "shareRole": false
                }
            }
        """.trimIndent()

        val response = GetUserParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.12.0", response.apiVersion)
    }

    @Test
    fun `parse extracts user fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0",
                "user": {
                    "username": "sindre",
                    "email": "sindre@activeobjects.no",
                    "scrobblingEnabled": true,
                    "adminRole": false,
                    "settingsRole": true,
                    "downloadRole": true,
                    "uploadRole": false,
                    "playlistRole": true,
                    "coverArtRole": true,
                    "commentRole": true,
                    "podcastRole": true,
                    "streamRole": true,
                    "jukeboxRole": true,
                    "shareRole": false,
                    "folder": [1, 3]
                }
            }
        """.trimIndent()

        val response = GetUserParser.parse(JSONObject(jsonString))

        assertEquals("sindre", response.user.username)
        assertEquals("sindre@activeobjects.no", response.user.email)
        assertTrue(response.user.scrobblingEnabled)
        assertFalse(response.user.adminRole)
        assertTrue(response.user.settingsRole)
        assertTrue(response.user.downloadRole)
        assertFalse(response.user.uploadRole)
        assertTrue(response.user.playlistRole)
        assertTrue(response.user.coverArtRole)
        assertTrue(response.user.commentRole)
        assertTrue(response.user.podcastRole)
        assertTrue(response.user.streamRole)
        assertTrue(response.user.jukeboxRole)
        assertFalse(response.user.shareRole)
        assertEquals(listOf(1, 3), response.user.folders)
    }

    @Test
    fun `parse returns empty folder list when folder is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0",
                "user": {
                    "username": "sindre",
                    "email": "sindre@activeobjects.no",
                    "scrobblingEnabled": false,
                    "adminRole": false,
                    "settingsRole": false,
                    "downloadRole": false,
                    "uploadRole": false,
                    "playlistRole": false,
                    "coverArtRole": false,
                    "commentRole": false,
                    "podcastRole": false,
                    "streamRole": false,
                    "jukeboxRole": false,
                    "shareRole": false
                }
            }
        """.trimIndent()

        val response = GetUserParser.parse(JSONObject(jsonString))

        assertEquals(emptyList(), response.user.folders)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val innerJson = """
            {
                "username": "sindre",
                "email": "sindre@activeobjects.no",
                "scrobblingEnabled": false,
                "adminRole": false,
                "settingsRole": false,
                "downloadRole": false,
                "uploadRole": false,
                "playlistRole": false,
                "coverArtRole": false,
                "commentRole": false,
                "podcastRole": false,
                "streamRole": false,
                "jukeboxRole": false,
                "shareRole": false
            }
        """.trimIndent()

        val response = GetUserParser.parse(
            TestFixtures.navidromeResponseJson("user", innerJson)
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
