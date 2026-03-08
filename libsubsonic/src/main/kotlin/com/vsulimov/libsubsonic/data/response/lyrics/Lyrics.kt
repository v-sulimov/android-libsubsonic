package com.vsulimov.libsubsonic.data.response.lyrics

/**
 * Represents lyrics for a song returned by the Subsonic server.
 *
 * @property artist The artist name associated with the lyrics.
 * @property title The song title associated with the lyrics.
 * @property value The lyrics text content.
 */
data class Lyrics(
    val artist: String? = null,
    val title: String? = null,
    val value: String? = null
)
