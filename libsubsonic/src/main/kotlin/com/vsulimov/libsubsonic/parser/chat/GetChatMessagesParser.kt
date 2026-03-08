package com.vsulimov.libsubsonic.parser.chat

import com.vsulimov.libsubsonic.data.response.chat.ChatMessage
import com.vsulimov.libsubsonic.data.response.chat.ChatMessagesResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getChatMessages` response payload.
 */
internal object GetChatMessagesParser {

    /**
     * Parses the "subsonic-response" object into a [ChatMessagesResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [ChatMessagesResponse].
     */
    fun parse(json: JSONObject): ChatMessagesResponse {
        val containerObj = json.optJSONObject("chatMessages")
        val messages = containerObj?.parseList("chatMessage") { msgJson ->
            ChatMessage(
                username = msgJson.optString("username"),
                time = msgJson.optLong("time"),
                message = msgJson.optString("message")
            )
        } ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return ChatMessagesResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            messages = messages
        )
    }
}
