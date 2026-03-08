package com.vsulimov.libsubsonic.parser.annotation

import com.vsulimov.libsubsonic.data.response.annotation.UnstarResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `unstar` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object UnstarParser {

    /**
     * Parses the "subsonic-response" object into an [UnstarResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [UnstarResponse].
     */
    fun parse(json: JSONObject): UnstarResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return UnstarResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
