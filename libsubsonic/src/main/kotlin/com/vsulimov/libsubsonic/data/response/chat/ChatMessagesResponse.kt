package com.vsulimov.libsubsonic.data.response.chat

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getChatMessages` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property messages The list of chat messages returned by the server.
 */
data class ChatMessagesResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val messages: List<ChatMessage>
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
