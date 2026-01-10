package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetSimilarSongsParserTest {

    @Test
    fun `parse returns songs list from similarSongs2 array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "similarSongs2": {
                    "song": [
                        {
                            "id": "1631",
                            "parent": "1628",
                            "isDir": false,
                            "title": "A Whiter Shade Of Pale",
                            "album": "Medusa",
                            "artist": "Annie Lennox",
                            "track": 3,
                            "coverArt": "1628",
                            "size": 5068173,
                            "contentType": "audio/mpeg",
                            "suffix": "mp3",
                            "duration": 316,
                            "bitRate": 128,
                            "path": "Annie Lennox/Medusa/03 - A Whiter Shade Of Pale.MP3",
                            "isVideo": false,
                            "created": "2004-11-08T22:21:17.000Z",
                            "albumId": "471",
                            "artistId": "305",
                            "type": "music"
                        },
                        {
                            "id": "4654",
                            "parent": "4643",
                            "isDir": false,
                            "title": "somebodys somebody",
                            "album": "christina aguilera",
                            "artist": "christina aguilera",
                            "track": 8,
                            "year": 1999,
                            "genre": "Pop",
                            "coverArt": "4643",
                            "size": 6039760,
                            "contentType": "audio/mpeg",
                            "suffix": "mp3",
                            "duration": 302,
                            "bitRate": 160,
                            "path": "Christina Aguilera/Album/08-cps-christina_aguilera--somebodys_somebody.mp3",
                            "isVideo": false,
                            "created": "2004-11-25T22:18:53.000Z",
                            "albumId": "698",
                            "type": "music"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetSimilarSongsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.11.0", response.apiVersion)
        assertEquals(2, response.songs.size)

        val first = response.songs[0]
        assertEquals("1631", first.id)
        assertEquals("1628", first.parent)
        assertEquals(false, first.isDir)
        assertEquals("A Whiter Shade Of Pale", first.title)
        assertEquals("Medusa", first.album)
        assertEquals("Annie Lennox", first.artist)
        assertEquals(3, first.track)
        assertEquals("1628", first.coverArt)
        assertEquals(5068173L, first.size)
        assertEquals("audio/mpeg", first.contentType)
        assertEquals("mp3", first.suffix)
        assertEquals(316, first.duration)
        assertEquals(128, first.bitRate)
        assertEquals("Annie Lennox/Medusa/03 - A Whiter Shade Of Pale.MP3", first.path)
        assertEquals(false, first.isVideo)
        assertEquals("2004-11-08T22:21:17.000Z", first.created)
        assertEquals("471", first.albumId)
        assertEquals("305", first.artistId)
        assertEquals("music", first.type)
        assertNull(first.year)
        assertNull(first.genre)

        val second = response.songs[1]
        assertEquals("4654", second.id)
        assertEquals("somebodys somebody", second.title)
        assertEquals(1999, second.year)
        assertEquals("Pop", second.genre)
        assertNull(second.artistId)
    }

    @Test
    fun `parse returns empty list when similarSongs2 container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0"
            }
        """.trimIndent()

        val response = GetSimilarSongsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse returns empty list when song array is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "similarSongs2": {}
            }
        """.trimIndent()

        val response = GetSimilarSongsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.songs.size)
    }

    @Test
    fun `parse handles single song object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.11.0",
                "similarSongs2": {
                    "song": {
                        "id": "1635",
                        "parent": "1628",
                        "isDir": false,
                        "title": "Cold",
                        "album": "Medusa",
                        "artist": "Annie Lennox",
                        "track": 6,
                        "coverArt": "1628",
                        "size": 4123456,
                        "contentType": "audio/mpeg",
                        "suffix": "mp3",
                        "duration": 240,
                        "bitRate": 128,
                        "path": "Annie Lennox/Medusa/06 - Cold.MP3",
                        "isVideo": false,
                        "created": "2004-11-08T22:21:20.000Z",
                        "albumId": "471",
                        "artistId": "305",
                        "type": "music"
                    }
                }
            }
        """.trimIndent()

        val response = GetSimilarSongsParser.parse(JSONObject(jsonString))

        assertEquals(1, response.songs.size)
        assertEquals("1635", response.songs[0].id)
        assertEquals("Cold", response.songs[0].title)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetSimilarSongsParser.parse(
            TestFixtures.navidromeResponseJson("similarSongs2", """{"song": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertEquals(0, response.songs.size)
    }
}
