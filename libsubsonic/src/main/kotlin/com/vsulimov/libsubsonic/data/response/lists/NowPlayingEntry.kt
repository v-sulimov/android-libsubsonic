package com.vsulimov.libsubsonic.data.response.lists

import com.vsulimov.libsubsonic.data.response.browsing.Child

/**
 * Represents a single entry in the now-playing list, combining song details with
 * playback context for the user currently playing the track.
 *
 * @property username The username of the user playing the track.
 * @property minutesAgo The number of minutes ago playback started.
 * @property playerId The ID of the player used for playback.
 * @property playerName The name of the player, if provided by the server.
 * @property song The song currently being played.
 */
data class NowPlayingEntry(
    val username: String,
    val minutesAgo: Int,
    val playerId: Int,
    val playerName: String? = null,
    val song: Child
)
