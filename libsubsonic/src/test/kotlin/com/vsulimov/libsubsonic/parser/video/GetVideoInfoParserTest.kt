package com.vsulimov.libsubsonic.parser.video

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetVideoInfoParserTest {

    @Test
    fun `parse extracts captions audio tracks and conversions`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.14.0",
                "videoInfo": {
                    "id": "7058",
                    "captions": [
                        { "id": "0", "name": "Planes 2.srt" }
                    ],
                    "audioTrack": [
                        { "id": "1", "name": "English", "languageCode": "eng" },
                        { "id": "3", "name": "Danish", "languageCode": "dan" },
                        { "id": "4", "name": "Finnish", "languageCode": "fin" }
                    ],
                    "conversion": [
                        { "id": "37", "bitRate": 1000 }
                    ]
                }
            }
        """.trimIndent()

        val response = GetVideoInfoParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.14.0", response.apiVersion)

        val info = response.videoInfo
        assertEquals("7058", info.id)

        assertEquals(1, info.captions.size)
        assertEquals("0", info.captions[0].id)
        assertEquals("Planes 2.srt", info.captions[0].name)

        assertEquals(3, info.audioTracks.size)
        assertEquals("1", info.audioTracks[0].id)
        assertEquals("English", info.audioTracks[0].name)
        assertEquals("eng", info.audioTracks[0].languageCode)
        assertEquals("3", info.audioTracks[1].id)
        assertEquals("Danish", info.audioTracks[1].name)
        assertEquals("dan", info.audioTracks[1].languageCode)
        assertEquals("4", info.audioTracks[2].id)
        assertEquals("Finnish", info.audioTracks[2].name)
        assertEquals("fin", info.audioTracks[2].languageCode)

        assertEquals(1, info.conversions.size)
        assertEquals("37", info.conversions[0].id)
        assertEquals(1000, info.conversions[0].bitRate)
    }

    @Test
    fun `parse handles single captions object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.14.0",
                "videoInfo": {
                    "id": "7058",
                    "captions": { "id": "0", "name": "Planes 2.srt" }
                }
            }
        """.trimIndent()

        val response = GetVideoInfoParser.parse(JSONObject(jsonString))

        assertEquals(1, response.videoInfo.captions.size)
        assertEquals("Planes 2.srt", response.videoInfo.captions[0].name)
    }

    @Test
    fun `parse handles single audioTrack object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.14.0",
                "videoInfo": {
                    "id": "7058",
                    "audioTrack": { "id": "1", "name": "English", "languageCode": "eng" }
                }
            }
        """.trimIndent()

        val response = GetVideoInfoParser.parse(JSONObject(jsonString))

        assertEquals(1, response.videoInfo.audioTracks.size)
        assertEquals("English", response.videoInfo.audioTracks[0].name)
        assertEquals("eng", response.videoInfo.audioTracks[0].languageCode)
    }

    @Test
    fun `parse returns empty lists when all sub-elements are absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.14.0",
                "videoInfo": {
                    "id": "7058"
                }
            }
        """.trimIndent()

        val response = GetVideoInfoParser.parse(JSONObject(jsonString))

        assertEquals("7058", response.videoInfo.id)
        assertEquals(0, response.videoInfo.captions.size)
        assertEquals(0, response.videoInfo.audioTracks.size)
        assertEquals(0, response.videoInfo.conversions.size)
    }

    @Test
    fun `parse returns default videoInfo when videoInfo object is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.14.0"
            }
        """.trimIndent()

        val response = GetVideoInfoParser.parse(JSONObject(jsonString))

        assertEquals("", response.videoInfo.id)
        assertEquals(0, response.videoInfo.captions.size)
        assertEquals(0, response.videoInfo.audioTracks.size)
        assertEquals(0, response.videoInfo.conversions.size)
    }

    @Test
    fun `parse handles audioTrack with absent languageCode`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.14.0",
                "videoInfo": {
                    "id": "7058",
                    "audioTrack": [
                        { "id": "1", "name": "Unknown" }
                    ]
                }
            }
        """.trimIndent()

        val response = GetVideoInfoParser.parse(JSONObject(jsonString))

        assertNull(response.videoInfo.audioTracks[0].languageCode)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetVideoInfoParser.parse(
            TestFixtures.navidromeResponseJson("videoInfo", """{"id": "1"}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
