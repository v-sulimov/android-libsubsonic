package com.vsulimov.libsubsonic.parser.browsing

import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class GetArtistParserTest {

    @Test
    fun `parse handles detailed artist and album metadata from navidrome`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0 (cc3cca60)",
                "openSubsonic": true,
                "artist": {
                  "id": "art1",
                  "name": "Bring Me The Horizon",
                  "coverArt": "ca1",
                  "albumCount": 1,
                  "artistImageUrl": "http://img.com/artist",
                  "sortName": "bring me the horizon",
                  "roles": ["artist", "albumartist"],
                  "album": [
                    {
                      "id": "alb1",
                      "name": "Sempiternal",
                      "artist": "Bring Me The Horizon",
                      "artistId": "art1",
                      "coverArt": "ca2",
                      "songCount": 14,
                      "duration": 3460,
                      "playCount": 6,
                      "created": "2025-12-30T00:30:33Z",
                      "year": 2013,
                      "genre": "Alternative Rock",
                      "played": "2026-01-05T14:09:34Z",
                      "userRating": 0,
                      "isCompilation": false,
                      "sortName": "sempiternal",
                      "displayArtist": "Bring Me The Horizon"
                    }
                  ]
                }
            }
        """.trimIndent()

        val response = GetArtistParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("navidrome", response.serverType)

        val artist = response.artist
        assertEquals("art1", artist.id)
        assertEquals("bring me the horizon", artist.sortName)
        assertEquals(listOf("artist", "albumartist"), artist.roles)
        assertEquals(1, artist.albums.size)

        val album = artist.albums[0]
        assertEquals("alb1", album.id)
        assertEquals("Sempiternal", album.name)
        assertEquals(6, album.playCount)
        assertEquals(0, album.userRating)
        assertEquals(false, album.isCompilation)
        assertEquals("sempiternal", album.sortName)
        assertEquals("Bring Me The Horizon", album.displayArtist)
    }
}
