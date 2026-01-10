package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.response.playlists.DeletePlaylistResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `deletePlaylist` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object DeletePlaylistParser {

    /**
     * Parses the "subsonic-response" object into a [DeletePlaylistResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [DeletePlaylistResponse].
     */
    fun parse(json: JSONObject): DeletePlaylistResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return DeletePlaylistResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
