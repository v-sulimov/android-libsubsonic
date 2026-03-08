package com.vsulimov.libsubsonic.parser.annotation

import com.vsulimov.libsubsonic.data.response.annotation.ScrobbleResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `scrobble` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object ScrobbleParser {

    /**
     * Parses the "subsonic-response" object into a [ScrobbleResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [ScrobbleResponse].
     */
    fun parse(json: JSONObject): ScrobbleResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return ScrobbleResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
