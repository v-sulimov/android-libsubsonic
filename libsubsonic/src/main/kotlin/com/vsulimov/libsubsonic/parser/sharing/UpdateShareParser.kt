package com.vsulimov.libsubsonic.parser.sharing

import com.vsulimov.libsubsonic.data.response.sharing.UpdateShareResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `updateShare` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object UpdateShareParser {

    /**
     * Parses the "subsonic-response" object into an [UpdateShareResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [UpdateShareResponse].
     */
    fun parse(json: JSONObject): UpdateShareResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return UpdateShareResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
