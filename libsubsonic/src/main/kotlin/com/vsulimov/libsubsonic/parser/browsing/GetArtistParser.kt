package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Album
import com.vsulimov.libsubsonic.data.response.browsing.Artist
import com.vsulimov.libsubsonic.data.response.browsing.ArtistResponse
import org.json.JSONObject

/**
 * Internal parser for the `getArtist` response.
 */
internal object GetArtistParser {

    /**
     * Parses the "subsonic-response" object into [ArtistResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     */
    fun parse(json: JSONObject): ArtistResponse {
        val artistObj = json.optJSONObject("artist")
        val artist = if (artistObj != null) {
            val albums = mutableListOf<Album>()
            val albumArray = artistObj.optJSONArray("album")

            if (albumArray != null) {
                for (i in 0 until albumArray.length()) {
                    albums.add(parseAlbum(albumArray.getJSONObject(i)))
                }
            } else {
                artistObj.optJSONObject("album")?.let {
                    albums.add(parseAlbum(it))
                }
            }

            val roles = mutableListOf<String>()
            val rolesArray = artistObj.optJSONArray("roles")
            if (rolesArray != null) {
                for (i in 0 until rolesArray.length()) {
                    roles.add(rolesArray.getString(i))
                }
            }

            Artist(
                id = artistObj.optString("id"),
                name = artistObj.optString("name"),
                coverArt = artistObj.optString("coverArt").ifEmpty { null },
                albumCount = artistObj.optInt("albumCount", 0),
                artistImageUrl = artistObj.optString("artistImageUrl").ifEmpty { null },
                musicBrainzId = artistObj.optString("musicBrainzId").ifEmpty { null },
                sortName = artistObj.optString("sortName").ifEmpty { null },
                roles = roles,
                albums = albums
            )
        } else {
            Artist(id = "", name = "")
        }

        return ArtistResponse(
            status = json.optString("status", "ok"),
            apiVersion = json.optString("version", "Unknown"),
            serverType = json.optString("type").ifEmpty { null },
            serverVersion = json.optString("serverVersion").ifEmpty { null },
            isOpenSubsonic = json.optBoolean("openSubsonic", false),
            artist = artist
        )
    }

    private fun parseAlbum(json: JSONObject) = Album(
        id = json.optString("id"),
        name = json.optString("name"),
        artist = json.optString("artist").ifEmpty { null },
        artistId = json.optString("artistId").ifEmpty { null },
        coverArt = json.optString("coverArt").ifEmpty { null },
        songCount = if (json.has("songCount")) json.optInt("songCount") else null,
        duration = if (json.has("duration")) json.optInt("duration") else null,
        playCount = if (json.has("playCount")) json.optInt("playCount") else null,
        created = json.optString("created").ifEmpty { null },
        year = if (json.has("year")) json.optInt("year") else null,
        genre = json.optString("genre").ifEmpty { null },
        played = json.optString("played").ifEmpty { null },
        userRating = if (json.has("userRating")) json.optInt("userRating") else null,
        musicBrainzId = json.optString("musicBrainzId").ifEmpty { null },
        isCompilation = json.optBoolean("isCompilation", false),
        sortName = json.optString("sortName").ifEmpty { null },
        displayArtist = json.optString("displayArtist").ifEmpty { null },
        explicitStatus = json.optString("explicitStatus").ifEmpty { null }
    )
}
