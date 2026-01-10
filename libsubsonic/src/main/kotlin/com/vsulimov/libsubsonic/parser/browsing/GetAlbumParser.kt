package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Album
import com.vsulimov.libsubsonic.data.response.browsing.AlbumResponse
import com.vsulimov.libsubsonic.data.response.browsing.Child
import org.json.JSONObject

/**
 * Internal parser responsible for extracting [AlbumResponse] from the JSON response.
 */
internal object GetAlbumParser {

    /**
     * Parses the "subsonic-response" object into an [AlbumResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return An [AlbumResponse] containing metadata and the requested album with its songs.
     */
    fun parse(json: JSONObject): AlbumResponse {
        val albumObj = json.optJSONObject("album")
        val album = if (albumObj != null) {
            val songs = mutableListOf<Child>()
            val songArray = albumObj.optJSONArray("song")

            if (songArray != null) {
                for (i in 0 until songArray.length()) {
                    songs.add(GetSongParser.parseSong(songArray.getJSONObject(i)))
                }
            } else {
                albumObj.optJSONObject("song")?.let {
                    songs.add(GetSongParser.parseSong(it))
                }
            }

            Album(
                id = albumObj.optString("id"),
                name = albumObj.optString("name"),
                artist = albumObj.optString("artist").ifEmpty { null },
                artistId = albumObj.optString("artistId").ifEmpty { null },
                coverArt = albumObj.optString("coverArt").ifEmpty { null },
                songCount = if (albumObj.has("songCount")) albumObj.optInt("songCount") else null,
                duration = if (albumObj.has("duration")) albumObj.optInt("duration") else null,
                playCount = if (albumObj.has("playCount")) albumObj.optInt("playCount") else null,
                created = albumObj.optString("created").ifEmpty { null },
                year = if (albumObj.has("year")) albumObj.optInt("year") else null,
                genre = albumObj.optString("genre").ifEmpty { null },
                played = albumObj.optString("played").ifEmpty { null },
                userRating = if (albumObj.has("userRating")) albumObj.optInt("userRating") else null,
                musicBrainzId = albumObj.optString("musicBrainzId").ifEmpty { null },
                isCompilation = albumObj.optBoolean("isCompilation", false),
                sortName = albumObj.optString("sortName").ifEmpty { null },
                displayArtist = albumObj.optString("displayArtist").ifEmpty { null },
                explicitStatus = albumObj.optString("explicitStatus").ifEmpty { null },
                songs = songs
            )
        } else {
            Album(id = "", name = "")
        }

        return AlbumResponse(
            status = json.optString("status", "ok"),
            apiVersion = json.optString("version", "Unknown"),
            serverType = json.optString("type").ifEmpty { null },
            serverVersion = json.optString("serverVersion").ifEmpty { null },
            isOpenSubsonic = json.optBoolean("openSubsonic", false),
            album = album
        )
    }
}
