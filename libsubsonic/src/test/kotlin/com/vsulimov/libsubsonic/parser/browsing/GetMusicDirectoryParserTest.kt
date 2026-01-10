package com.vsulimov.libsubsonic.parser.browsing

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.json.JSONObject

class GetMusicDirectoryParserTest {

    @Test
    fun `parse correctly extracts directory and detailed children`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0 (cc3cca60)",
                "openSubsonic": true,
                "directory": {
                  "id": "1vtfI276kzSA8UTjxjHCYP",
                  "name": "Hollywood Undead",
                  "playCount": 12,
                  "played": "2026-01-10T00:42:55.372Z",
                  "albumCount": 23,
                  "child": [
                    {
                      "id": "264a3PhgGe7NjWCETLE3yW",
                      "parent": "1vtfI276kzSA8UTjxjHCYP",
                      "isDir": true,
                      "title": "Black Dahlia Remixes",
                      "album": "Black Dahlia Remixes",
                      "artist": "Hollywood Undead",
                      "year": 2008,
                      "genre": "Alternative",
                      "coverArt": "al-264a3PhgGe7NjWCETLE3yW_694a88f8",
                      "duration": 731,
                      "created": "2025-12-30T00:30:39.352604922Z",
                      "artistId": "1vtfI276kzSA8UTjxjHCYP",
                      "songCount": 3,
                      "isVideo": false,
                      "playCount": 5,
                      "played": "2026-01-10T00:42:55.372Z",
                      "mediaType": "album",
                      "explicitStatus": "explicit",
                      "displayArtist": "Hollywood Undead"
                    }
                  ]
                }
            }
        """.trimIndent()

        val response = GetMusicDirectoryParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.16.1", response.apiVersion)
        assertEquals("navidrome", response.serverType)

        val dir = response.directory
        assertEquals("1vtfI276kzSA8UTjxjHCYP", dir.id)
        assertEquals("Hollywood Undead", dir.name)
        assertEquals(12, dir.playCount)
        assertEquals(23, dir.albumCount)

        val child = dir.children[0]
        assertEquals("264a3PhgGe7NjWCETLE3yW", child.id)
        assertTrue(child.isDir)
        assertEquals("Black Dahlia Remixes", child.title)
        assertEquals("Hollywood Undead", child.artist)
        assertEquals(2008, child.year)
        assertEquals("Alternative", child.genre)
        assertEquals(731, child.duration)
        assertEquals("1vtfI276kzSA8UTjxjHCYP", child.artistId)
        assertEquals(3, child.songCount)
        assertEquals(false, child.isVideo)
        assertEquals(5, child.playCount)
        assertEquals("album", child.mediaType)
        assertEquals("explicit", child.explicitStatus)
        assertEquals("Hollywood Undead", child.displayArtist)
    }
}
