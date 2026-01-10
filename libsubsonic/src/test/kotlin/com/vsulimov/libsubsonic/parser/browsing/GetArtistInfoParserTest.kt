package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetArtistInfoParserTest {

    @Test
    fun `parse handles artist info with similar artists`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
                "openSubsonic": true,
                "artistInfo2": {
                    "biography": "An English rock band.",
                    "musicBrainzId": "mbid-1",
                    "lastFmUrl": "https://www.last.fm/music/Example",
                    "smallImageUrl": "https://img.com/small.jpg",
                    "mediumImageUrl": "https://img.com/medium.jpg",
                    "largeImageUrl": "https://img.com/large.jpg",
                    "similarArtist": [
                        {
                            "id": "art1",
                            "name": "Similar Artist 1",
                            "coverArt": "ca1",
                            "artistImageUrl": "https://img.com/artist1.jpg"
                        },
                        {
                            "id": "art2",
                            "name": "Similar Artist 2"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetArtistInfoParser.parse(JSONObject(jsonString))

        TestFixtures.assertNavidromeMetadata(response)

        val info = response.artistInfo
        assertEquals("An English rock band.", info.biography)
        assertEquals("mbid-1", info.musicBrainzId)
        assertEquals("https://www.last.fm/music/Example", info.lastFmUrl)
        assertEquals("https://img.com/small.jpg", info.smallImageUrl)
        assertEquals("https://img.com/medium.jpg", info.mediumImageUrl)
        assertEquals("https://img.com/large.jpg", info.largeImageUrl)
        assertEquals(2, info.similarArtists.size)
        assertEquals("art1", info.similarArtists[0].id)
        assertEquals("Similar Artist 1", info.similarArtists[0].name)
        assertEquals("ca1", info.similarArtists[0].coverArt)
        assertEquals("https://img.com/artist1.jpg", info.similarArtists[0].artistImageUrl)
        assertEquals("art2", info.similarArtists[1].id)
        assertEquals("Similar Artist 2", info.similarArtists[1].name)
    }

    @Test
    fun `parse handles missing artist info fields`() {
        val jsonString = """
            {
                "status": "ok",
                "artistInfo2": {}
            }
        """.trimIndent()

        val response = GetArtistInfoParser.parse(JSONObject(jsonString))

        assertNull(response.artistInfo.biography)
        assertEquals(0, response.artistInfo.similarArtists.size)
    }

    @Test
    fun `parse handles single similar artist object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "artistInfo2": {
                    "similarArtist": {
                        "id": "art1",
                        "name": "Solo Similar Artist"
                    }
                }
            }
        """.trimIndent()

        val response = GetArtistInfoParser.parse(JSONObject(jsonString))

        assertEquals(1, response.artistInfo.similarArtists.size)
        assertEquals("art1", response.artistInfo.similarArtists[0].id)
        assertEquals("Solo Similar Artist", response.artistInfo.similarArtists[0].name)
    }

    @Test
    fun `parse returns default artist info when artistInfo2 key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetArtistInfoParser.parse(JSONObject(jsonString))

        assertNull(response.artistInfo.biography)
        assertNull(response.artistInfo.musicBrainzId)
        assertNull(response.artistInfo.lastFmUrl)
        assertTrue(response.artistInfo.similarArtists.isEmpty())
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetArtistInfoParser.parse(
            TestFixtures.navidromeResponseJson("artistInfo2", """{"similarArtist": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
