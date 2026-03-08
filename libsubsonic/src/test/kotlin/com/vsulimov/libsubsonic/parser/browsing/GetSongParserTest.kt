package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetSongParserTest {

    @Test
    fun `parse handles detailed song metadata from navidrome`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
                "openSubsonic": true,
                "song": {
                    "id": "WqAPmCayJ8iPFUgmbGMZAI",
                    "parent": "2OqHgQVE4pgwSUP364PpOa",
                    "isDir": false,
                    "title": "The House Of Wolves",
                    "album": "Sempiternal",
                    "artist": "Bring Me The Horizon",
                    "track": 2,
                    "year": 2013,
                    "genre": "Alternative Rock",
                    "coverArt": "al-2OqHgQVE4pgwSUP364PpOa_0",
                    "size": 45455133,
                    "contentType": "audio/flac",
                    "suffix": "flac",
                    "duration": 205,
                    "bitRate": 1748,
                    "path": "Bring Me The Horizon/Sempiternal/02 - The House Of Wolves.flac",
                    "playCount": 1,
                    "created": "2025-12-30T00:30:33.753687424Z",
                    "albumId": "2OqHgQVE4pgwSUP364PpOa",
                    "artistId": "1gPutbMtakxQ5g1IMgkxD3",
                    "type": "music",
                    "isVideo": false,
                    "played": "2026-01-04T19:50:18.75Z",
                    "bpm": 0,
                    "comment": "",
                    "sortName": "the house of wolves",
                    "mediaType": "song",
                    "musicBrainzId": "",
                    "isrc": [],
                    "genres": [
                        {
                            "name": "Alternative Rock"
                        }
                    ],
                    "replayGain": {},
                    "channelCount": 2,
                    "samplingRate": 44100,
                    "bitDepth": 24,
                    "moods": [],
                    "artists": [
                        {
                            "id": "1gPutbMtakxQ5g1IMgkxD3",
                            "name": "Bring Me The Horizon"
                        }
                    ],
                    "displayArtist": "Bring Me The Horizon",
                    "albumArtists": [
                        {
                            "id": "1gPutbMtakxQ5g1IMgkxD3",
                            "name": "Bring Me The Horizon"
                        }
                    ],
                    "displayAlbumArtist": "Bring Me The Horizon",
                    "contributors": [],
                    "displayComposer": "",
                    "explicitStatus": ""
                }
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        TestFixtures.assertNavidromeMetadata(response)

        val song = response.song
        assertEquals("WqAPmCayJ8iPFUgmbGMZAI", song.id)
        assertEquals("The House Of Wolves", song.title)
        assertEquals("audio/flac", song.contentType)
        assertEquals(24, song.bitDepth)
        assertEquals(44100, song.samplingRate)
        assertEquals("Bring Me The Horizon/Sempiternal/02 - The House Of Wolves.flac", song.path)
        assertEquals("2OqHgQVE4pgwSUP364PpOa", song.parent)

        // OpenSubsonic fields
        assertEquals(1, song.artists.size)
        assertEquals("1gPutbMtakxQ5g1IMgkxD3", song.artists[0].id)
        assertEquals(1, song.albumArtists.size)
        assertEquals("Bring Me The Horizon", song.displayAlbumArtist)
        assertEquals(1, song.genres.size)
        assertEquals("Alternative Rock", song.genres[0].name)
        assertEquals(0, song.isrc.size)
        assertEquals(0, song.moods.size)
        assertEquals(0, song.contributors.size)
        assertNotNull(song.replayGain)
        assertNull(song.starred)
    }

    @Test
    fun `parse captures starred field when present`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "song": {
                    "id": "1012",
                    "parent": "1008",
                    "isDir": false,
                    "title": "Head Over Heels",
                    "album": "Balls To The Wall",
                    "artist": "Accept",
                    "coverArt": "1008",
                    "size": 6219527,
                    "contentType": "audio/mpeg",
                    "suffix": "mp3",
                    "duration": 259,
                    "bitRate": 192,
                    "path": "Accept/Balls To The Wall/Accept - Head Over Heels - 04.mp3",
                    "isVideo": false,
                    "created": "2004-11-27T17:22:35.000Z",
                    "starred": "2015-01-03T21:12:10.070Z",
                    "albumId": "411",
                    "artistId": "278",
                    "type": "music"
                }
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        assertEquals("2015-01-03T21:12:10.070Z", response.song.starred)
    }

    @Test
    fun `parse returns default song when song key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        assertEquals("", response.song.id)
        assertEquals("", response.song.title)
        assertEquals(false, response.song.isDir)
        assertTrue(response.song.artists.isEmpty())
    }

    @Test
    fun `parse returns null for missing replayGain fields instead of NaN`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "song": {
                    "id": "s1",
                    "isDir": false,
                    "title": "Test",
                    "replayGain": {
                        "trackGain": -6.5
                    }
                }
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        val rg = response.song.replayGain
        assertNotNull(rg)
        assertEquals(-6.5, rg.trackGain)
        assertNull(rg.albumGain)
        assertNull(rg.artistGain)
        assertNull(rg.trackPeak)
        assertNull(rg.albumPeak)
    }

    @Test
    fun `parse returns null replayGain when object is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "song": {
                    "id": "s1",
                    "isDir": false,
                    "title": "Test"
                }
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        assertNull(response.song.replayGain)
    }

    @Test
    fun `parse handles all replayGain fields present`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "song": {
                    "id": "s1",
                    "isDir": false,
                    "title": "Test",
                    "replayGain": {
                        "albumGain": -8.5,
                        "artistGain": -7.2,
                        "trackPeak": 0.98,
                        "albumPeak": 0.95,
                        "trackGain": -6.5
                    }
                }
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        val rg = response.song.replayGain
        assertNotNull(rg)
        assertEquals(-8.5, rg.albumGain)
        assertEquals(-7.2, rg.artistGain)
        assertEquals(0.98, rg.trackPeak)
        assertEquals(0.95, rg.albumPeak)
        assertEquals(-6.5, rg.trackGain)
    }

    @Test
    fun `parse handles empty replayGain object with all null fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "song": {
                    "id": "s1",
                    "isDir": false,
                    "title": "Test",
                    "replayGain": {}
                }
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        val rg = response.song.replayGain
        assertNotNull(rg)
        assertNull(rg.albumGain)
        assertNull(rg.artistGain)
        assertNull(rg.trackPeak)
        assertNull(rg.albumPeak)
        assertNull(rg.trackGain)
    }

    @Test
    fun `parse treats empty strings as null for optional string fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "song": {
                    "id": "s1",
                    "isDir": false,
                    "title": "Test",
                    "album": "",
                    "artist": "",
                    "genre": "",
                    "coverArt": "",
                    "path": "",
                    "musicBrainzId": "",
                    "displayComposer": ""
                }
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        assertNull(response.song.album)
        assertNull(response.song.artist)
        assertNull(response.song.genre)
        assertNull(response.song.coverArt)
        assertNull(response.song.path)
        assertNull(response.song.musicBrainzId)
        assertNull(response.song.displayComposer)
    }

    @Test
    fun `parse handles isrc and moods arrays with values`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "song": {
                    "id": "s1",
                    "isDir": false,
                    "title": "Test",
                    "isrc": ["USAT21301002", "GBAYE0601498"],
                    "moods": ["happy", "energetic"]
                }
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        assertEquals(listOf("USAT21301002", "GBAYE0601498"), response.song.isrc)
        assertEquals(listOf("happy", "energetic"), response.song.moods)
    }

    @Test
    fun `parse handles missing integer fields as null`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "song": {
                    "id": "s1",
                    "isDir": false,
                    "title": "Test"
                }
            }
        """.trimIndent()

        val response = GetSongParser.parse(JSONObject(jsonString))

        assertNull(response.song.track)
        assertNull(response.song.year)
        assertNull(response.song.duration)
        assertNull(response.song.bitRate)
        assertNull(response.song.bitDepth)
        assertNull(response.song.samplingRate)
        assertNull(response.song.channelCount)
        assertNull(response.song.size)
        assertNull(response.song.bpm)
        assertNull(response.song.playCount)
        assertNull(response.song.songCount)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetSongParser.parse(
            TestFixtures.navidromeResponseJson("song", """{"id": "x", "title": "X", "isDir": false}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
