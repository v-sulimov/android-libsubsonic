package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.ArtistInfo
import com.vsulimov.libsubsonic.data.response.browsing.ArtistInfoResponse
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getArtistInfo2` response payload.
 */
internal object GetArtistInfoParser {

    /**
     * Parses the `subsonic-response` object into an [ArtistInfoResponse].
     *
     * If the `artistInfo2` element is missing the response carries an empty [ArtistInfo].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [ArtistInfoResponse].
     */
    fun parse(json: JSONObject): ArtistInfoResponse {
        val artistInfo = json.optJSONObject("artistInfo2")?.let { obj ->
            ArtistInfo(
                biography = obj.optStringOrNull("biography"),
                musicBrainzId = obj.optStringOrNull("musicBrainzId"),
                lastFmUrl = obj.optStringOrNull("lastFmUrl"),
                smallImageUrl = obj.optStringOrNull("smallImageUrl"),
                mediumImageUrl = obj.optStringOrNull("mediumImageUrl"),
                largeImageUrl = obj.optStringOrNull("largeImageUrl"),
                similarArtists = obj.parseList("similarArtist", GetArtistsParser::parseSingleArtist)
            )
        } ?: ArtistInfo()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return ArtistInfoResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            artistInfo = artistInfo
        )
    }
}
