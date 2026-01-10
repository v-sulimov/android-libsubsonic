package com.vsulimov.libsubsonic.data.response.video

/**
 * Represents an audio track available for a video.
 *
 * @property id The unique identifier for the audio track.
 * @property name The display name of the audio track (for example, "English").
 * @property languageCode The ISO 639-2 language code of the audio track (for example, "eng").
 */
data class AudioTrack(val id: String, val name: String, val languageCode: String? = null)
