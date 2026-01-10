package com.vsulimov.libsubsonic.parser.system

import com.vsulimov.libsubsonic.data.response.system.PingResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses a `ping` response payload into a [PingResponse].
 */
internal object PingParser {

    /**
     * Extracts ping metadata from the "subsonic-response" object.
     *
     * @param json The "subsonic-response" JSONObject.
     * @return The parsed [PingResponse].
     */
    fun parse(json: JSONObject): PingResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return PingResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
