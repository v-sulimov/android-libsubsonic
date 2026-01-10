package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents ReplayGain metadata for a track.
 */
data class ReplayGain(
    val albumGain: Double? = null,
    val artistGain: Double? = null,
    val trackPeak: Double? = null,
    val albumPeak: Double? = null,
    val trackGain: Double? = null
)
