package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetIndexesParserTest {

    @Test
    fun `parse handles nested indexes shortcuts and children`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
                "openSubsonic": true,
                "indexes": {
                    "lastModified": 1767054630000,
                    "ignoredArticles": "The El La Los Las Le Les Os As O A",
                    "shortcut": [
                        { "id": "11", "name": "Audio books" }
                    ],
                    "index": [
                        {
                            "name": "A",
                            "artist": [
                                {
                                    "id": "a1",
                                    "name": "AC/DC",
                                    "coverArt": "ca1",
                                    "artistImageUrl": "http://img.com/1",
                                    "starred": "2013-11-02T12:30:00"
                                }
                            ]
                        }
                    ],
                    "child": [
                        {
                            "id": "111",
                            "parent": "11",
                            "title": "Dancing Queen",
                            "isDir": false
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetIndexesParser.parse(JSONObject(jsonString))

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(1767054630000L, response.lastModified)
        assertEquals("The El La Los Las Le Les Os As O A", response.ignoredArticles)

        assertEquals(expected = 1, actual = response.indexes.size)
        assertEquals(expected = "A", actual = response.indexes[0].name)
        val artist = response.indexes[0].artists[0]
        assertEquals(expected = "a1", actual = artist.id)
        assertEquals(expected = "AC/DC", actual = artist.name)
        assertEquals(expected = "ca1", actual = artist.coverArt)
        assertEquals(expected = "http://img.com/1", actual = artist.artistImageUrl)
        assertEquals(expected = "2013-11-02T12:30:00", actual = artist.starred)

        assertEquals("11", response.shortcuts[0].id)
        assertEquals("Audio books", response.shortcuts[0].name)

        assertEquals("111", response.children[0].id)
        assertEquals("11", response.children[0].parent)
    }

    @Test
    fun `parse returns empty collections when indexes key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetIndexesParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertTrue(response.indexes.isEmpty())
        assertTrue(response.shortcuts.isEmpty())
        assertTrue(response.children.isEmpty())
        assertEquals(0L, response.lastModified)
        assertEquals("", response.ignoredArticles)
    }

    @Test
    fun `parse handles single index object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "indexes": {
                    "ignoredArticles": "",
                    "index": {
                        "name": "B",
                        "artist": [
                            { "id": "b1", "name": "Beatles" }
                        ]
                    }
                }
            }
        """.trimIndent()

        val response = GetIndexesParser.parse(JSONObject(jsonString))

        assertEquals(1, response.indexes.size)
        assertEquals("B", response.indexes[0].name)
        assertEquals("b1", response.indexes[0].artists[0].id)
    }

    @Test
    fun `parse handles single artist inside index instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "indexes": {
                    "ignoredArticles": "",
                    "index": [
                        {
                            "name": "I",
                            "artist": { "id": "126", "name": "Iron Maiden" }
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetIndexesParser.parse(JSONObject(jsonString))

        assertEquals(1, response.indexes[0].artists.size)
        assertEquals("Iron Maiden", response.indexes[0].artists[0].name)
    }

    @Test
    fun `parse handles single shortcut object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "indexes": {
                    "ignoredArticles": "",
                    "shortcut": { "id": "11", "name": "Audio books" }
                }
            }
        """.trimIndent()

        val response = GetIndexesParser.parse(JSONObject(jsonString))

        assertEquals(1, response.shortcuts.size)
        assertEquals("11", response.shortcuts[0].id)
        assertEquals("Audio books", response.shortcuts[0].name)
    }

    @Test
    fun `parse handles single child object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "indexes": {
                    "ignoredArticles": "",
                    "child": {
                        "id": "200",
                        "parent": "11",
                        "title": "Track 1",
                        "isDir": false
                    }
                }
            }
        """.trimIndent()

        val response = GetIndexesParser.parse(JSONObject(jsonString))

        assertEquals(1, response.children.size)
        assertEquals("200", response.children[0].id)
        assertEquals("Track 1", response.children[0].title)
    }

    @Test
    fun `parse handles artist with absent optional fields`() {
        val jsonString = """
            {
                "status": "ok",
                "indexes": {
                    "ignoredArticles": "",
                    "index": [
                        {
                            "name": "K",
                            "artist": [
                                { "id": "141", "name": "Kvelertak" }
                            ]
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetIndexesParser.parse(JSONObject(jsonString))

        val artist = response.indexes[0].artists[0]
        assertEquals("141", artist.id)
        assertEquals("Kvelertak", artist.name)
        assertNull(artist.coverArt)
        assertNull(artist.artistImageUrl)
        assertNull(artist.starred)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetIndexesParser.parse(
            TestFixtures.navidromeResponseJson("indexes", """{"ignoredArticles": ""}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertTrue(response.indexes.isEmpty())
    }
}
