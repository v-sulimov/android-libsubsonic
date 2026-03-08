package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import org.json.JSONObject

class GetNewestPodcastsParserTest {

    @Test
    fun `parse returns episodes from newestPodcasts container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "newestPodcasts": {
                    "episode": [
                        {
                            "id": "7390",
                            "parent": "7389",
                            "isDir": false,
                            "title": "Jonas Gahr Støre",
                            "album": "NRK – Hallo P3",
                            "artist": "Podcast",
                            "year": 2015,
                            "coverArt": "7389",
                            "size": 41808585,
                            "contentType": "audio/mpeg",
                            "suffix": "mp3",
                            "duration": 2619,
                            "bitRate": 128,
                            "isVideo": false,
                            "created": "2015-09-07T20:07:31.000Z",
                            "artistId": "453",
                            "type": "podcast",
                            "streamId": "7410",
                            "channelId": "17",
                            "description": "Jonas Gahr Støre answers listeners questions.",
                            "status": "completed",
                            "publishDate": "2015-09-07T15:29:00.000Z"
                        },
                        {
                            "id": "7402",
                            "parent": "7397",
                            "isDir": false,
                            "title": "06.09.15 - Lunsjkake",
                            "album": "NRK – Lunsjkake",
                            "artist": "Podcast",
                            "year": 2015,
                            "coverArt": "7397",
                            "size": 47540895,
                            "contentType": "audio/mpeg",
                            "suffix": "mp3",
                            "duration": 2978,
                            "bitRate": 128,
                            "isVideo": false,
                            "created": "2015-09-07T20:07:31.000Z",
                            "artistId": "453",
                            "type": "podcast",
                            "streamId": "7411",
                            "channelId": "21",
                            "description": "Hva har skjedd siden sist?",
                            "status": "completed",
                            "publishDate": "2015-09-07T11:40:00.000Z"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetNewestPodcastsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.13.0", response.apiVersion)
        assertEquals(2, response.episodes.size)

        val episode = response.episodes[0]
        assertEquals("7390", episode.id)
        assertEquals("7410", episode.streamId)
        assertEquals("17", episode.channelId)
        assertEquals("Jonas Gahr Støre", episode.title)
        assertEquals("NRK – Hallo P3", episode.album)
        assertEquals("Podcast", episode.artist)
        assertEquals("453", episode.artistId)
        assertEquals("podcast", episode.type)
        assertEquals(2015, episode.year)
        assertEquals(2619, episode.duration)
        assertEquals(128, episode.bitRate)
        assertFalse(episode.isVideo)
        assertEquals("2015-09-07T20:07:31.000Z", episode.created)
        assertEquals("2015-09-07T15:29:00.000Z", episode.publishDate)
        assertEquals("completed", episode.status)
    }

    @Test
    fun `parse handles missing optional episode fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "newestPodcasts": {
                    "episode": [
                        {
                            "id": "1",
                            "channelId": "1",
                            "title": "Episode 1",
                            "status": "completed",
                            "isDir": false
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetNewestPodcastsParser.parse(JSONObject(jsonString))

        val episode = response.episodes[0]
        assertNull(episode.streamId)
        assertNull(episode.album)
        assertNull(episode.artist)
        assertNull(episode.artistId)
        assertNull(episode.type)
        assertNull(episode.created)
        assertNull(episode.publishDate)
    }

    @Test
    fun `parse handles single episode object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "newestPodcasts": {
                    "episode": {
                        "id": "1",
                        "channelId": "1",
                        "title": "Episode 1",
                        "status": "completed",
                        "isDir": false
                    }
                }
            }
        """.trimIndent()

        val response = GetNewestPodcastsParser.parse(JSONObject(jsonString))

        assertEquals(1, response.episodes.size)
        assertEquals("1", response.episodes[0].id)
    }

    @Test
    fun `parse returns empty list when newestPodcasts container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0"
            }
        """.trimIndent()

        val response = GetNewestPodcastsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.episodes.size)
    }

    @Test
    fun `parse returns empty list when episode array is absent inside container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "newestPodcasts": {}
            }
        """.trimIndent()

        val response = GetNewestPodcastsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.episodes.size)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetNewestPodcastsParser.parse(
            TestFixtures.navidromeResponseJson("newestPodcasts", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
