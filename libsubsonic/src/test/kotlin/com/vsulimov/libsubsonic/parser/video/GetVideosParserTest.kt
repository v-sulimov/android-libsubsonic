package com.vsulimov.libsubsonic.parser.video

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetVideosParserTest {

    @Test
    fun `parse returns videos list from videos container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "videos": {
                    "video": [
                        {
                            "id": "7228",
                            "parent": "7217",
                            "title": "Ringeren i Notre Dame",
                            "album": "Barn",
                            "isDir": false,
                            "coverArt": "7217",
                            "created": "2011-04-15T11:47:20",
                            "duration": 5226,
                            "bitRate": 1339,
                            "size": 874815058,
                            "suffix": "avi",
                            "contentType": "video/avi",
                            "isVideo": true,
                            "path": "Barn/Ringeren i Notre Dame.avi",
                            "transcodedSuffix": "flv",
                            "transcodedContentType": "video/x-flv"
                        },
                        {
                            "id": "7229",
                            "parent": "7217",
                            "title": "Tarzan",
                            "album": "Barn",
                            "isDir": false,
                            "coverArt": "7217",
                            "created": "2011-04-27T20:41:24",
                            "duration": 5086,
                            "bitRate": 1791,
                            "size": 1138861232,
                            "suffix": "avi",
                            "contentType": "video/avi",
                            "isVideo": true,
                            "path": "Barn/Tarzan.avi",
                            "transcodedSuffix": "flv",
                            "transcodedContentType": "video/x-flv"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetVideosParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.8.0", response.apiVersion)
        assertEquals(2, response.videos.size)

        val first = response.videos[0]
        assertEquals("7228", first.id)
        assertEquals("7217", first.parent)
        assertEquals("Ringeren i Notre Dame", first.title)
        assertEquals("Barn", first.album)
        assertEquals(false, first.isDir)
        assertEquals("7217", first.coverArt)
        assertEquals("2011-04-15T11:47:20", first.created)
        assertEquals(5226, first.duration)
        assertEquals(1339, first.bitRate)
        assertEquals(874815058L, first.size)
        assertEquals("avi", first.suffix)
        assertEquals("video/avi", first.contentType)
        assertTrue(first.isVideo)
        assertEquals("Barn/Ringeren i Notre Dame.avi", first.path)
        assertEquals("flv", first.transcodedSuffix)
        assertEquals("video/x-flv", first.transcodedContentType)

        val second = response.videos[1]
        assertEquals("7229", second.id)
        assertEquals("Tarzan", second.title)
        assertEquals(5086, second.duration)
        assertEquals(1138861232L, second.size)
    }

    @Test
    fun `parse returns empty list when videos container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0"
            }
        """.trimIndent()

        val response = GetVideosParser.parse(JSONObject(jsonString))

        assertEquals(0, response.videos.size)
    }

    @Test
    fun `parse returns empty list when video array is absent inside container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "videos": {}
            }
        """.trimIndent()

        val response = GetVideosParser.parse(JSONObject(jsonString))

        assertEquals(0, response.videos.size)
    }

    @Test
    fun `parse handles single video object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "videos": {
                    "video": {
                        "id": "7231",
                        "title": "UP",
                        "isDir": false,
                        "isVideo": true,
                        "path": "Barn/UP.avi"
                    }
                }
            }
        """.trimIndent()

        val response = GetVideosParser.parse(JSONObject(jsonString))

        assertEquals(1, response.videos.size)
        assertEquals("7231", response.videos[0].id)
        assertEquals("UP", response.videos[0].title)
        assertTrue(response.videos[0].isVideo)
        assertEquals("Barn/UP.avi", response.videos[0].path)
    }

    @Test
    fun `parse handles optional fields as null when absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.8.0",
                "videos": {
                    "video": [
                        {
                            "id": "7231",
                            "title": "UP",
                            "isDir": false,
                            "isVideo": true
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetVideosParser.parse(JSONObject(jsonString))

        val video = response.videos[0]
        assertNull(video.parent)
        assertNull(video.album)
        assertNull(video.coverArt)
        assertNull(video.duration)
        assertNull(video.bitRate)
        assertNull(video.size)
        assertNull(video.suffix)
        assertNull(video.contentType)
        assertNull(video.transcodedSuffix)
        assertNull(video.transcodedContentType)
        assertNull(video.path)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetVideosParser.parse(
            TestFixtures.navidromeResponseJson("videos", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
