package com.vsulimov.libsubsonic.parser.browsing

import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class GetGenresParserTest {

    @Test
    fun `parse correctly extracts genres and metadata from navidrome response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0 (cc3cca60)",
                "openSubsonic": true,
                "genres": {
                    "genre": [
                        { "value": "Rock", "songCount": 89, "albumCount": 12 },
                        { "value": "Alternative Rock", "songCount": 50, "albumCount": 3 }
                    ]
                }
            }
        """.trimIndent()

        val response = GetGenresParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.16.1", response.apiVersion)
        assertEquals("navidrome", response.serverType)

        val result = response.genres
        assertEquals(expected = 2, actual = result.size)
        assertEquals(expected = "Rock", actual = result[0].value)
        assertEquals(expected = 89, actual = result[0].songCount)
        assertEquals(expected = 12, actual = result[0].albumCount)
        assertEquals(expected = "Alternative Rock", actual = result[1].value)
    }

    @Test
    fun `parse handles single genre object`() {
        val jsonString = """
            {
                "status": "ok",
                "genres": {
                    "genre": { "value": "Ambient", "songCount": 12, "albumCount": 1 }
                }
            }
        """.trimIndent()

        val response = GetGenresParser.parse(JSONObject(jsonString))

        assertEquals(expected = 1, actual = response.genres.size)
        assertEquals(expected = "Ambient", actual = response.genres[0].value)
    }
}
