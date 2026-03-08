package com.vsulimov.libsubsonic.parser.playqueue

import com.vsulimov.libsubsonic.data.response.playqueue.SavePlayQueueResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `savePlayQueue` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object SavePlayQueueParser {

    /**
     * Parses the "subsonic-response" object into a [SavePlayQueueResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [SavePlayQueueResponse].
     */
    fun parse(json: JSONObject): SavePlayQueueResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return SavePlayQueueResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
