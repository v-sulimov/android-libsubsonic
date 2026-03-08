package com.vsulimov.libsubsonic.parser.sharing

import com.vsulimov.libsubsonic.data.response.sharing.DeleteShareResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `deleteShare` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object DeleteShareParser {

    /**
     * Parses the "subsonic-response" object into a [DeleteShareResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [DeleteShareResponse].
     */
    fun parse(json: JSONObject): DeleteShareResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return DeleteShareResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
