package com.vsulimov.libsubsonic.parser.playqueue

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetPlayQueueParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0"
            }
        """.trimIndent()

        val response = GetPlayQueueParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.12.0", response.apiVersion)
    }

    @Test
    fun `parse returns null playQueue when absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0"
            }
        """.trimIndent()

        val response = GetPlayQueueParser.parse(JSONObject(jsonString))

        assertNull(response.playQueue)
    }

    @Test
    fun `parse extracts playQueue fields and entries`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0",
                "playQueue": {
                    "current": "133",
                    "position": 45000,
                    "username": "admin",
                    "changed": "2015-02-18T15:22:22.825Z",
                    "changedBy": "android",
                    "entry": [
                        {
                            "id": "132",
                            "isDir": false,
                            "title": "These Are Days",
                            "isVideo": false
                        },
                        {
                            "id": "133",
                            "isDir": false,
                            "title": "Eat For Two",
                            "isVideo": false
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetPlayQueueParser.parse(JSONObject(jsonString))

        val playQueue = response.playQueue!!
        assertEquals("133", playQueue.current)
        assertEquals(45000L, playQueue.position)
        assertEquals("admin", playQueue.username)
        assertEquals("2015-02-18T15:22:22.825Z", playQueue.changed)
        assertEquals("android", playQueue.changedBy)
        assertEquals(2, playQueue.entries.size)
        assertEquals("132", playQueue.entries[0].id)
        assertEquals("These Are Days", playQueue.entries[0].title)
        assertEquals("133", playQueue.entries[1].id)
    }

    @Test
    fun `parse sets current and position to null when absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0",
                "playQueue": {
                    "username": "admin",
                    "changed": "2015-02-18T15:22:22.825Z",
                    "changedBy": "android"
                }
            }
        """.trimIndent()

        val response = GetPlayQueueParser.parse(JSONObject(jsonString))

        assertNull(response.playQueue!!.current)
        assertNull(response.playQueue.position)
        assertEquals(emptyList(), response.playQueue.entries)
    }

    @Test
    fun `parse handles single entry object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0",
                "playQueue": {
                    "username": "admin",
                    "changed": "2015-02-18T15:22:22.825Z",
                    "changedBy": "android",
                    "entry": {
                        "id": "132",
                        "isDir": false,
                        "title": "These Are Days",
                        "isVideo": false
                    }
                }
            }
        """.trimIndent()

        val response = GetPlayQueueParser.parse(JSONObject(jsonString))

        val playQueue = response.playQueue!!
        assertEquals(1, playQueue.entries.size)
        assertEquals("132", playQueue.entries[0].id)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetPlayQueueParser.parse(
            TestFixtures.navidromeResponseJson("playQueue", "{\"username\":\"\",\"changed\":\"\",\"changedBy\":\"\"}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
