package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Artist
import com.vsulimov.libsubsonic.data.response.browsing.ArtistResponse
import com.vsulimov.libsubsonic.parser.optStringList
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getArtist` response payload.
 */
internal object GetArtistParser {

    /**
     * Parses the `subsonic-response` object into an [ArtistResponse].
     *
     * If the `artist` element is missing the response carries an empty placeholder [Artist].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [ArtistResponse].
     */
    fun parse(json: JSONObject): ArtistResponse {
        val artist = json.optJSONObject("artist")?.let { obj ->
            Artist(
                id = obj.optString("id"),
                name = obj.optString("name"),
                albumCount = obj.optInt("albumCount", 0),
                coverArt = obj.optStringOrNull("coverArt"),
                artistImageUrl = obj.optStringOrNull("artistImageUrl"),
                musicBrainzId = obj.optStringOrNull("musicBrainzId"),
                sortName = obj.optStringOrNull("sortName"),
                roles = obj.optStringList("roles"),
                albums = obj.parseList("album", GetAlbumParser::parseAlbum)
            )
        } ?: Artist(id = "", name = "")

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
