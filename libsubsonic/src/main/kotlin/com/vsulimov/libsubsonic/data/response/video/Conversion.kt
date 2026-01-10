package com.vsulimov.libsubsonic.data.response.video

/**
 * Represents a pre-configured video conversion profile available on the server.
 *
 * @property id The unique identifier for the conversion profile.
 * @property bitRate The target bit rate in kilobits per second for this conversion.
 */
data class Conversion(val id: String, val bitRate: Int? = null)
