package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.json.JSONObject

class GetGenresParserTest {

    @Test
    fun `parse correctly extracts genres and metadata from navidrome response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
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

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(expected = 2, actual = response.genres.size)
        assertEquals(expected = "Rock", actual = response.genres[0].value)
        assertEquals(expected = 89, actual = response.genres[0].songCount)
        assertEquals(expected = 12, actual = response.genres[0].albumCount)
        assertEquals(expected = "Alternative Rock", actual = response.genres[1].value)
    }

    @Test
    fun `parse handles xml-style genre entries with name field`() {
        val jsonString = """
            {
                "status": "ok",
                "genres": {
                    "genre": [
                        { "name": "Electronic", "songCount": 28, "albumCount": 6 },
                        { "name": "Hard Rock", "songCount": 6, "albumCount": 2 }
                    ]
                }
            }
        """.trimIndent()

        val response = GetGenresParser.parse(JSONObject(jsonString))

        assertEquals(expected = 2, actual = response.genres.size)
        assertEquals(expected = "Electronic", actual = response.genres[0].value)
        assertEquals(expected = 28, actual = response.genres[0].songCount)
        assertEquals(expected = 6, actual = response.genres[0].albumCount)
    }

    @Test
    fun `parse handles single genre object instead of array`() {
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

    @Test
    fun `parse returns empty list when genres key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetGenresParser.parse(JSONObject(jsonString))

        assertTrue(actual = response.genres.isEmpty())
    }

    @Test
    fun `parse returns empty list when genre array is empty`() {
        val jsonString = """
            {
                "status": "ok",
                "genres": {
                    "genre": []
                }
            }
        """.trimIndent()

        val response = GetGenresParser.parse(JSONObject(jsonString))

        assertTrue(actual = response.genres.isEmpty())
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetGenresParser.parse(
            TestFixtures.navidromeResponseJson("genres", """{"genre": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertTrue(actual = response.genres.isEmpty())
    }
}
