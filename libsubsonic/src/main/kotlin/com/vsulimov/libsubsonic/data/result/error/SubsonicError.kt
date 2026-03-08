package com.vsulimov.libsubsonic.data.result.error

/**
 * Represents an error returned by the Subsonic API or generated internally by the library.
 *
 * @property code The [SubsonicErrorCode] identifying the error category.
 * @property message A human-readable error description.
 */
data class SubsonicError(val code: SubsonicErrorCode, override val message: String) : Exception(message) {

    /**
     * Skips stack trace capture since this class is a structured error carrier,
     * not a throwable intended for debugging call sites.
     */
    override fun fillInStackTrace(): Throwable = this
}
