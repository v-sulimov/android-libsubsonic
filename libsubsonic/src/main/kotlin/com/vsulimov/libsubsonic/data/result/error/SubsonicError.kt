package com.vsulimov.libsubsonic.data.result.error

/**
 * Represents an error within the Subsonic ecosystem.
 *
 * @property code The Subsonic error code (e.g., 40 for "Wrong password"), or 0 for generic error.
 * @property message A human-readable error description.
 */
data class SubsonicError(val code: Int, override val message: String) : Exception(message)
