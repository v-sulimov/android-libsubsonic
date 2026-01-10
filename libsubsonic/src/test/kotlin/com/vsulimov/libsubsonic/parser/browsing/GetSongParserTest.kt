package com.vsulimov.libsubsonic.parser.browsing

import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class GetSongParserTest {

    @Test
    fun `parse handles detailed song metadata from navidrome`() {
        val jsonString = """
            {
              "subsonic-response": {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0 (cc3cca60)",
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
                  "channelCount": 2,
                  "samplingRate": 44100,
                  "bitDepth": 24,
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
                  "explicitStatus": ""
                }
              }
            }
        """.trimIndent()

        val root = JSONObject(jsonString)
        val response = GetSongParser.parse(root.getJSONObject("subsonic-response"))

        assertEquals("ok", response.status)
        assertEquals("navidrome", response.serverType)

        val song = response.song
        assertEquals("WqAPmCayJ8iPFUgmbGMZAI", song.id)
        assertEquals("The House Of Wolves", song.title)
        assertEquals("audio/flac", song.contentType)
        assertEquals(24, song.bitDepth)
        assertEquals(44100, song.samplingRate)
        assertEquals("Bring Me The Horizon/Sempiternal/02 - The House Of Wolves.flac", song.path)

        // OpenSubsonic fields
        assertEquals(1, song.artists.size)
        assertEquals("1gPutbMtakxQ5g1IMgkxD3", song.artists[0].id)
        assertEquals(1, song.albumArtists.size)
        assertEquals("Bring Me The Horizon", song.displayAlbumArtist)
    }
}
