package com.vsulimov.libsubsonic.data.response.video

/**
 * Represents a captions track available for a video.
 *
 * @property id The unique identifier for the captions track.
 * @property name The display name of the captions file (for example, "Planes 2.srt").
 */
data class Caption(val id: String, val name: String)
