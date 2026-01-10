package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents ReplayGain metadata for a track.
 *
 * @property albumGain The album-level gain adjustment in dB.
 * @property artistGain The artist-level gain adjustment in dB.
 * @property trackPeak The track-level peak value.
 * @property albumPeak The album-level peak value.
 * @property trackGain The track-level gain adjustment in dB.
 */
data class ReplayGain(
    val albumGain: Double? = null,
    val artistGain: Double? = null,
    val trackPeak: Double? = null,
    val albumPeak: Double? = null,
    val trackGain: Double? = null
)
