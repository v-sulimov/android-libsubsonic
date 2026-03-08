package com.vsulimov.libsubsonic.data.response.chat

/**
 * Represents a single chat message returned by the server.
 *
 * @property username The username of the message author.
 * @property time The timestamp of the message in milliseconds since epoch.
 * @property message The message text.
 */
data class ChatMessage(
    val username: String,
    val time: Long,
    val message: String
)
