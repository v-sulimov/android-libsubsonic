package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.ArtistInfo
import com.vsulimov.libsubsonic.data.response.browsing.ArtistInfoResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getArtistInfo2` response payload.
 */
internal object GetArtistInfoParser {

    /**
     * Parses the "subsonic-response" object into an [ArtistInfoResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [ArtistInfoResponse].
     */
    fun parse(json: JSONObject): ArtistInfoResponse {
        val infoObj = json.optJSONObject("artistInfo2")
        val artistInfo = if (infoObj != null) {
            ArtistInfo(
                biography = infoObj.optString("biography").ifEmpty { null },
                musicBrainzId = infoObj.optString("musicBrainzId").ifEmpty { null },
                lastFmUrl = infoObj.optString("lastFmUrl").ifEmpty { null },
                smallImageUrl = infoObj.optString("smallImageUrl").ifEmpty { null },
                mediumImageUrl = infoObj.optString("mediumImageUrl").ifEmpty { null },
                largeImageUrl = infoObj.optString("largeImageUrl").ifEmpty { null },
                similarArtists = infoObj.parseList("similarArtist") { GetArtistsParser.parseSingleArtist(it) }
            )
        } else {
            ArtistInfo()
        }

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
