package com.vsulimov.libsubsonic.parser.sharing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetSharesParserTest {

    @Test
    fun `parse returns shares with entries from shares container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.6.0",
                "shares": {
                    "share": [
                        {
                            "id": "1",
                            "url": "http://sindre.subsonic.org/share/sKoYn",
                            "description": "Check this out",
                            "username": "sindre",
                            "created": "2011-06-04T12:34:56",
                            "lastVisited": "2011-06-04T13:14:15",
                            "expires": "2013-06-04T00:00:00",
                            "visitCount": 0,
                            "entry": [
                                {
                                    "id": "111",
                                    "parent": "11",
                                    "title": "Dancing Queen",
                                    "isDir": false,
                                    "album": "Arrival",
                                    "artist": "ABBA",
                                    "track": 7,
                                    "year": 1978,
                                    "genre": "Pop",
                                    "coverArt": "24",
                                    "size": 8421341,
                                    "contentType": "audio/mpeg",
                                    "suffix": "mp3",
                                    "duration": 146,
                                    "bitRate": 128,
                                    "path": "ABBA/Arrival/Dancing Queen.mp3"
                                },
                                {
                                    "id": "112",
                                    "parent": "11",
                                    "title": "Money, Money, Money",
                                    "isDir": false,
                                    "album": "Arrival",
                                    "artist": "ABBA",
                                    "track": 7,
                                    "year": 1978,
                                    "genre": "Pop",
                                    "coverArt": "25",
                                    "size": 4910028,
                                    "contentType": "audio/flac",
                                    "suffix": "flac",
                                    "transcodedContentType": "audio/mpeg",
                                    "transcodedSuffix": "mp3",
                                    "duration": 208,
                                    "bitRate": 128,
                                    "path": "ABBA/Arrival/Money, Money, Money.mp3"
                                }
                            ]
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetSharesParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.6.0", response.apiVersion)
        assertEquals(1, response.shares.size)

        val share = response.shares[0]
        assertEquals("1", share.id)
        assertEquals("http://sindre.subsonic.org/share/sKoYn", share.url)
        assertEquals("Check this out", share.description)
        assertEquals("sindre", share.username)
        assertEquals("2011-06-04T12:34:56", share.created)
        assertEquals("2011-06-04T13:14:15", share.lastVisited)
        assertEquals("2013-06-04T00:00:00", share.expires)
        assertEquals(0, share.visitCount)
        assertEquals(2, share.entries.size)

        val firstEntry = share.entries[0]
        assertEquals("111", firstEntry.id)
        assertEquals("Dancing Queen", firstEntry.title)
        assertEquals("ABBA", firstEntry.artist)
        assertEquals(146, firstEntry.duration)

        val secondEntry = share.entries[1]
        assertEquals("112", secondEntry.id)
        assertEquals("Money, Money, Money", secondEntry.title)
        assertEquals("audio/flac", secondEntry.contentType)
        assertEquals("audio/mpeg", secondEntry.transcodedContentType)
        assertEquals("mp3", secondEntry.transcodedSuffix)
    }

    @Test
    fun `parse handles single share object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.6.0",
                "shares": {
                    "share": {
                        "id": "1",
                        "url": "http://sindre.subsonic.org/share/sKoYn",
                        "username": "sindre",
                        "created": "2011-06-04T12:34:56",
                        "visitCount": 0
                    }
                }
            }
        """.trimIndent()

        val response = GetSharesParser.parse(JSONObject(jsonString))

        assertEquals(1, response.shares.size)
        assertEquals("1", response.shares[0].id)
    }

    @Test
    fun `parse handles missing optional share fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.6.0",
                "shares": {
                    "share": [
                        {
                            "id": "1",
                            "url": "http://sindre.subsonic.org/share/sKoYn",
                            "username": "sindre",
                            "created": "2011-06-04T12:34:56",
                            "visitCount": 3
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetSharesParser.parse(JSONObject(jsonString))

        val share = response.shares[0]
        assertNull(share.description)
        assertNull(share.lastVisited)
        assertNull(share.expires)
        assertEquals(emptyList(), share.entries)
    }

    @Test
    fun `parse returns empty list when shares container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.6.0"
            }
        """.trimIndent()

        val response = GetSharesParser.parse(JSONObject(jsonString))

        assertEquals(0, response.shares.size)
    }

    @Test
    fun `parse returns empty list when share array is absent inside container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.6.0",
                "shares": {}
            }
        """.trimIndent()

        val response = GetSharesParser.parse(JSONObject(jsonString))

        assertEquals(0, response.shares.size)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetSharesParser.parse(
            TestFixtures.navidromeResponseJson("shares", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
