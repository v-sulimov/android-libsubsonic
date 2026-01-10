package com.vsulimov.libsubsonic.data.result.error

/**
 * Represents an error returned by the Subsonic API.
 *
 * @property code The Subsonic error code (for example, 40 for "Wrong password").
 * @property message A human-readable error description.
 */
data class SubsonicError(val code: Int, override val message: String) : Exception(message)
