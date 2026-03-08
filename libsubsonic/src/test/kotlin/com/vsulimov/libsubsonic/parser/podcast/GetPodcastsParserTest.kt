package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetPodcastsParserTest {

    @Test
    fun `parse returns channels with episodes`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "podcasts": {
                    "channel": [
                        {
                            "id": "1",
                            "url": "http://downloads.bbc.co.uk/podcasts/fivelive/drkarl/rss.xml",
                            "title": "Dr Karl and the Naked Scientist",
                            "description": "Dr Chris Smith with the latest news from the world of science.",
                            "coverArt": "pod-1",
                            "originalImageUrl": "http://downloads.bbc.co.uk/podcasts/fivelive/drkarl/drkarl.jpg",
                            "status": "completed",
                            "episode": [
                                {
                                    "id": "34",
                                    "streamId": "523",
                                    "channelId": "1",
                                    "title": "Scorpions have re-evolved eyes",
                                    "description": "This week Dr Chris fills us in on science.",
                                    "publishDate": "2011-02-03T14:46:43",
                                    "status": "completed",
                                    "parent": "11",
                                    "isDir": false,
                                    "year": 2011,
                                    "genre": "Podcast",
                                    "coverArt": "24",
                                    "size": 78421341,
                                    "contentType": "audio/mpeg",
                                    "suffix": "mp3",
                                    "duration": 3146,
                                    "bitRate": 128,
                                    "path": "Podcast/drkarl/20110203.mp3"
                                }
                            ]
                        },
                        {
                            "id": "3",
                            "url": "http://foo.bar.com/xyz.rss",
                            "status": "error",
                            "errorMessage": "Not found."
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetPodcastsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.13.0", response.apiVersion)
        assertEquals(2, response.channels.size)

        val channel = response.channels[0]
        assertEquals("1", channel.id)
        assertEquals("http://downloads.bbc.co.uk/podcasts/fivelive/drkarl/rss.xml", channel.url)
        assertEquals("Dr Karl and the Naked Scientist", channel.title)
        assertEquals("pod-1", channel.coverArt)
        assertEquals("completed", channel.status)
        assertEquals(1, channel.episodes.size)

        val episode = channel.episodes[0]
        assertEquals("34", episode.id)
        assertEquals("523", episode.streamId)
        assertEquals("1", episode.channelId)
        assertEquals("Scorpions have re-evolved eyes", episode.title)
        assertEquals("2011-02-03T14:46:43", episode.publishDate)
        assertEquals("completed", episode.status)
        assertEquals(3146, episode.duration)
        assertEquals(128, episode.bitRate)
        assertEquals("audio/mpeg", episode.contentType)

        val errorChannel = response.channels[1]
        assertEquals("3", errorChannel.id)
        assertEquals("error", errorChannel.status)
        assertEquals("Not found.", errorChannel.errorMessage)
        assertEquals(emptyList(), errorChannel.episodes)
    }

    @Test
    fun `parse handles missing optional channel fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "podcasts": {
                    "channel": [
                        {
                            "id": "1",
                            "url": "http://example.com/rss.xml",
                            "status": "completed"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetPodcastsParser.parse(JSONObject(jsonString))

        val channel = response.channels[0]
        assertNull(channel.title)
        assertNull(channel.description)
        assertNull(channel.coverArt)
        assertNull(channel.originalImageUrl)
        assertNull(channel.errorMessage)
        assertEquals(emptyList(), channel.episodes)
    }

    @Test
    fun `parse handles missing optional episode fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "podcasts": {
                    "channel": [
                        {
                            "id": "1",
                            "url": "http://example.com/rss.xml",
                            "status": "completed",
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
                    ]
                }
            }
        """.trimIndent()

        val response = GetPodcastsParser.parse(JSONObject(jsonString))

        val episode = response.channels[0].episodes[0]
        assertNull(episode.streamId)
        assertNull(episode.description)
        assertNull(episode.publishDate)
        assertNull(episode.duration)
        assertNull(episode.bitRate)
        assertNull(episode.path)
    }

    @Test
    fun `parse handles single channel object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "podcasts": {
                    "channel": {
                        "id": "1",
                        "url": "http://example.com/rss.xml",
                        "status": "completed"
                    }
                }
            }
        """.trimIndent()

        val response = GetPodcastsParser.parse(JSONObject(jsonString))

        assertEquals(1, response.channels.size)
        assertEquals("1", response.channels[0].id)
    }

    @Test
    fun `parse returns empty list when podcasts container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0"
            }
        """.trimIndent()

        val response = GetPodcastsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.channels.size)
    }

    @Test
    fun `parse returns empty list when channel array is absent inside container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "podcasts": {}
            }
        """.trimIndent()

        val response = GetPodcastsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.channels.size)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetPodcastsParser.parse(
            TestFixtures.navidromeResponseJson("podcasts", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
