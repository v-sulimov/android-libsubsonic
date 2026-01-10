package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Artist
import com.vsulimov.libsubsonic.data.response.browsing.ArtistResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getArtist` response payload.
 */
internal object GetArtistParser {

    /**
     * Parses the "subsonic-response" object into an [ArtistResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [ArtistResponse].
     */
    fun parse(json: JSONObject): ArtistResponse {
        val artistObj = json.optJSONObject("artist")
        val artist = if (artistObj != null) {
            val albums = artistObj.parseList("album") { GetAlbumParser.parseAlbum(it) }
            val roles = artistObj.optJSONArray("roles")?.let { array ->
                (0 until array.length()).map { i -> array.getString(i) }
            } ?: emptyList()

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

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return ArtistResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            artist = artist
        )
    }
}
