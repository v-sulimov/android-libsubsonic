package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import com.vsulimov.libsubsonic.data.response.browsing.MusicFolder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.json.JSONObject

class GetMusicFoldersParserTest {

    @Test
    fun `parse handles multiple folders with integer IDs`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
                "openSubsonic": true,
                "musicFolders": {
                    "musicFolder": [
                        { "id": 1, "name": "Music Library" },
                        { "id": 2, "name": "Podcasts" }
                    ]
                }
            }
        """.trimIndent()

        val response = GetMusicFoldersParser.parse(JSONObject(jsonString))

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(expected = 2, actual = response.musicFolders.size)
        assertEquals(expected = MusicFolder(id = "1", name = "Music Library"), actual = response.musicFolders[0])
        assertEquals(expected = MusicFolder(id = "2", name = "Podcasts"), actual = response.musicFolders[1])
    }

    @Test
    fun `parse handles single folder as object`() {
        val jsonString = """
            {
                "status": "ok",
                "musicFolders": {
                    "musicFolder": { "id": 1, "name": "Music Library" }
                }
            }
        """.trimIndent()

        val response = GetMusicFoldersParser.parse(JSONObject(jsonString))

        assertEquals(expected = 1, actual = response.musicFolders.size)
        assertEquals(expected = MusicFolder(id = "1", name = "Music Library"), actual = response.musicFolders[0])
    }

    @Test
    fun `parse handles empty musicFolders container`() {
        val jsonString = """
            {
                "status": "ok",
                "musicFolders": {}
            }
        """.trimIndent()

        val response = GetMusicFoldersParser.parse(JSONObject(jsonString))

        assertTrue(
            actual = response.musicFolders.isEmpty(),
            message = "Result should be empty when no folders are present"
        )
    }

    @Test
    fun `parse returns empty list when musicFolders key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetMusicFoldersParser.parse(JSONObject(jsonString))

        assertTrue(actual = response.musicFolders.isEmpty())
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetMusicFoldersParser.parse(
            TestFixtures.navidromeResponseJson("musicFolders", """{"musicFolder": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertTrue(actual = response.musicFolders.isEmpty())
    }
}
