package com.vsulimov.libsubsonic.parser.browsing

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
                "serverVersion": "0.59.0 (cc3cca60)",
                "openSubsonic": true,
                "musicFolders": {
                    "musicFolder": [
                        { "id": 1, "name": "Music Library" },
                        { "id": 2, "name": "Podcasts" }
                    ]
                }
            }
        """.trimIndent()
        val json = JSONObject(jsonString)

        val response = GetMusicFoldersParser.parse(json)

        assertEquals("ok", response.status)
        assertEquals("1.16.1", response.apiVersion)
        assertEquals("navidrome", response.serverType)
        assertEquals("0.59.0 (cc3cca60)", response.serverVersion)
        assertEquals(true, response.isOpenSubsonic)

        val result = response.musicFolders
        assertEquals(expected = 2, actual = result.size)
        assertEquals(expected = MusicFolder(id = "1", name = "Music Library"), actual = result[0])
        assertEquals(expected = MusicFolder(id = "2", name = "Podcasts"), actual = result[1])
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
        val json = JSONObject(jsonString)

        val response = GetMusicFoldersParser.parse(json)

        val result = response.musicFolders
        assertEquals(expected = 1, actual = result.size)
        assertEquals(expected = MusicFolder(id = "1", name = "Music Library"), actual = result[0])
    }

    @Test
    fun `parse handles empty response gracefully`() {
        val jsonString = """
            {
                "status": "ok",
                "musicFolders": {}
            }
        """.trimIndent()
        val json = JSONObject(jsonString)

        val response = GetMusicFoldersParser.parse(json)

        assertTrue(
            actual = response.musicFolders.isEmpty(),
            message = "Result should be empty when no folders are present"
        )
    }
}
