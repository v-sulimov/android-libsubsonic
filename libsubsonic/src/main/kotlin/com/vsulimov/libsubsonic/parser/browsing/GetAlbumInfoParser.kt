package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.AlbumInfo
import com.vsulimov.libsubsonic.data.response.browsing.AlbumInfoResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `getAlbumInfo2` response payload.
 */
internal object GetAlbumInfoParser {

    /**
     * Parses the "subsonic-response" object into an [AlbumInfoResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [AlbumInfoResponse].
     */
    fun parse(json: JSONObject): AlbumInfoResponse {
        val infoObj = json.optJSONObject("albumInfo")
        val albumInfo = if (infoObj != null) {
            AlbumInfo(
                notes = infoObj.optString("notes").ifEmpty { null },
                musicBrainzId = infoObj.optString("musicBrainzId").ifEmpty { null },
                lastFmUrl = infoObj.optString("lastFmUrl").ifEmpty { null },
                smallImageUrl = infoObj.optString("smallImageUrl").ifEmpty { null },
                mediumImageUrl = infoObj.optString("mediumImageUrl").ifEmpty { null },
                largeImageUrl = infoObj.optString("largeImageUrl").ifEmpty { null }
            )
        } else {
            AlbumInfo()
        }

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
