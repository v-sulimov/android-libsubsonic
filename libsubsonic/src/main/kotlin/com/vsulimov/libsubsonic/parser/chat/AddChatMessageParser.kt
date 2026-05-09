package com.vsulimov.libsubsonic.parser.chat

import com.vsulimov.libsubsonic.data.response.chat.AddChatMessageResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `addChatMessage` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object AddChatMessageParser {

    /**
     * Parses the `subsonic-response` object into an [AddChatMessageResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [AddChatMessageResponse].
     */
    fun parse(json: JSONObject): AddChatMessageResponse = json.envelopeOnly(::AddChatMessageResponse)
}
