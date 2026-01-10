package com.vsulimov.libsubsonic.parser.browsing

import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class GetIndexesParserTest {

    @Test
    fun `parse handles nested indexes and artists`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0 (cc3cca60)",
                "openSubsonic": true,
                "indexes": {
                    "lastModified": 1767054630000,
                    "ignoredArticles": "The El La Los Las Le Les Os As O A",
                    "index": [
                        {
                            "name": "A",
                            "artist": [
                                { 
                                    "id": "a1", 
                                    "name": "AC/DC", 
                                    "coverArt": "ca1", 
                                    "artistImageUrl": "http://img.com/1" 
                                }
                            ]
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetIndexesParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.16.1", response.apiVersion)
        assertEquals(1767054630000L, response.lastModified)
        assertEquals("The El La Los Las Le Les Os As O A", response.ignoredArticles)

        val result = response.indexes
        assertEquals(expected = 1, actual = result.size)
        assertEquals(expected = "A", actual = result[0].name)
        val artist = result[0].artists[0]
        assertEquals(expected = "a1", actual = artist.id)
        assertEquals(expected = "AC/DC", actual = artist.name)
        assertEquals(expected = "ca1", actual = artist.coverArt)
        assertEquals(expected = "http://img.com/1", actual = artist.artistImageUrl)
    }
}
