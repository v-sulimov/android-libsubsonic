package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.response.playlists.UpdatePlaylistResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `updatePlaylist` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object UpdatePlaylistParser {

    /**
     * Parses the "subsonic-response" object into an [UpdatePlaylistResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [UpdatePlaylistResponse].
     */
    fun parse(json: JSONObject): UpdatePlaylistResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return UpdatePlaylistResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
