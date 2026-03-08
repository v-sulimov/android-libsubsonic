package com.vsulimov.libsubsonic.data.response.jukebox

import com.vsulimov.libsubsonic.data.response.browsing.Child

/**
 * Represents the jukebox playback status returned by the `jukeboxControl` endpoint.
 *
 * When the [JukeboxAction.GET] action is used, [entries] contains the current playlist.
 * For all other actions, [entries] is empty.
 *
 * @property currentIndex The index of the currently playing track in the playlist.
 * @property playing Whether the jukebox is currently playing.
 * @property gain The current playback gain, between 0.0 and 1.0.
 * @property position The position in the current track in seconds.
 * @property entries The playlist entries, populated only for the [JukeboxAction.GET] action.
 */
data class JukeboxStatus(
    val currentIndex: Int,
    val playing: Boolean,
    val gain: Double,
    val position: Int? = null,
    val entries: List<Child> = emptyList()
)
