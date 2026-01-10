package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
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
                "serverVersion": "0.59.0",
                "openSubsonic": true,
                "directory": {
                    "id": "1vtfI276kzSA8UTjxjHCYP",
                    "parent": "root",
                    "name": "Hollywood Undead",
                    "starred": "2026-01-01T10:10:10Z",
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

        TestFixtures.assertNavidromeMetadata(response)

        val dir = response.directory
        assertEquals("1vtfI276kzSA8UTjxjHCYP", dir.id)
        assertEquals("root", dir.parent)
        assertEquals("Hollywood Undead", dir.name)
        assertEquals("2026-01-01T10:10:10Z", dir.starred)
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

    @Test
    fun `parse handles directory with empty children array`() {
        val jsonString = """
            {
                "status": "ok",
                "directory": {
                    "id": "dir1",
                    "name": "Empty Folder",
                    "child": []
                }
            }
        """.trimIndent()

        val response = GetMusicDirectoryParser.parse(JSONObject(jsonString))

        assertEquals("dir1", response.directory.id)
        assertTrue(response.directory.children.isEmpty())
    }

    @Test
    fun `parse handles single child object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "directory": {
                    "id": "dir2",
                    "name": "Single Child",
                    "child": {
                        "id": "song1",
                        "title": "Only Track",
                        "isDir": false
                    }
                }
            }
        """.trimIndent()

        val response = GetMusicDirectoryParser.parse(JSONObject(jsonString))

        assertEquals(1, response.directory.children.size)
        assertEquals("song1", response.directory.children[0].id)
        assertEquals("Only Track", response.directory.children[0].title)
    }

    @Test
    fun `parse returns default directory when directory key is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.16.1"
            }
        """.trimIndent()

        val response = GetMusicDirectoryParser.parse(JSONObject(jsonString))

        assertEquals("", response.directory.id)
        assertEquals("", response.directory.name)
        assertNull(response.directory.parent)
        assertTrue(response.directory.children.isEmpty())
    }

    @Test
    fun `parse handles directory with absent optional fields`() {
        val jsonString = """
            {
                "status": "ok",
                "directory": {
                    "id": "dir3",
                    "name": "Minimal"
                }
            }
        """.trimIndent()

        val response = GetMusicDirectoryParser.parse(JSONObject(jsonString))

        val dir = response.directory
        assertEquals("dir3", dir.id)
        assertNull(dir.parent)
        assertNull(dir.starred)
        assertNull(dir.playCount)
        assertNull(dir.albumCount)
        assertTrue(dir.children.isEmpty())
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetMusicDirectoryParser.parse(
            TestFixtures.navidromeResponseJson("directory", """{"id": "x", "name": "X", "child": []}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
