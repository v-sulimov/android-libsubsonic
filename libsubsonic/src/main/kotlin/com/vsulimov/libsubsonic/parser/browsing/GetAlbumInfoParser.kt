package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.AlbumInfo
import com.vsulimov.libsubsonic.data.response.browsing.AlbumInfoResponse
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `getAlbumInfo2` response payload.
 */
internal object GetAlbumInfoParser {

    /**
     * Parses the `subsonic-response` object into an [AlbumInfoResponse].
     *
     * If the `albumInfo` element is missing the response carries an empty [AlbumInfo].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [AlbumInfoResponse].
     */
    fun parse(json: JSONObject): AlbumInfoResponse {
        val albumInfo = json.optJSONObject("albumInfo")?.let { obj ->
            AlbumInfo(
                notes = obj.optStringOrNull("notes"),
                musicBrainzId = obj.optStringOrNull("musicBrainzId"),
                lastFmUrl = obj.optStringOrNull("lastFmUrl"),
                smallImageUrl = obj.optStringOrNull("smallImageUrl"),
                mediumImageUrl = obj.optStringOrNull("mediumImageUrl"),
                largeImageUrl = obj.optStringOrNull("largeImageUrl")
            )
        } ?: AlbumInfo()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return AlbumInfoResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            albumInfo = albumInfo
        )
    }
}
