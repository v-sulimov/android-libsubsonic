package com.vsulimov.libsubsonic.parser.system

import com.vsulimov.libsubsonic.data.response.system.SubsonicPingResponse
import org.json.JSONObject

/**
 * Internal parser responsible for extracting [SubsonicPingResponse] data from the JSON response.
 */
internal object SubsonicPingResponseParser {

    /**
     * Extracts ping metadata from the "subsonic-response" object.
     *
     * @param json The "subsonic-response" JSONObject.
     */
    fun parse(json: JSONObject): SubsonicPingResponse = SubsonicPingResponse(
        status = json.optString("status", "ok"),
        apiVersion = json.optString("version", "Unknown"),
        serverType = json.optString("type").ifEmpty { null },
        serverVersion = json.optString("serverVersion").ifEmpty { null },
        isOpenSubsonic = json.optBoolean("openSubsonic", false)
    )
}
