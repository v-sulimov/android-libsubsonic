package com.vsulimov.libsubsonic.parser.chat

import com.vsulimov.libsubsonic.data.response.chat.AddChatMessageResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `addChatMessage` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object AddChatMessageParser {

    /**
     * Parses the "subsonic-response" object into an [AddChatMessageResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [AddChatMessageResponse].
     */
    fun parse(json: JSONObject): AddChatMessageResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return AddChatMessageResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
