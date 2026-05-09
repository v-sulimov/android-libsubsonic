package com.vsulimov.libsubsonic.data.response.podcast

/**
 * Status of a podcast channel or episode as reported by the Subsonic server.
 *
 * @property value The string value used by the Subsonic API for this status.
 */
enum class PodcastStatus(val value: String) {

    /** Recently added; not yet downloaded. */
    NEW("new"),

    /** Currently downloading. */
    DOWNLOADING("downloading"),

    /** Download finished successfully. */
    COMPLETED("completed"),

    /** Download failed. The accompanying `errorMessage` describes the cause when available. */
    ERROR("error"),

    /** The channel or episode has been deleted by the user. */
    DELETED("deleted"),

    /** The episode was skipped by the user. */
    SKIPPED("skipped"),

    /** A status string the server reported that is not part of the Subsonic 1.16.1 spec. */
    UNKNOWN("");

    companion object {

        /**
         * Returns the [PodcastStatus] matching [value] case-insensitively, or [UNKNOWN]
         * if no spec-defined entry matches. The original wire value is preserved on
         * [UNKNOWN.value]'s instance only when the input matched [UNKNOWN] itself.
         *
         * @param value The status string returned by the server.
         * @return The corresponding enum entry, or [UNKNOWN] for unrecognised input.
         */
        fun fromValue(value: String): PodcastStatus =
            entries.firstOrNull { it != UNKNOWN && it.value.equals(value, ignoreCase = true) }
                ?: UNKNOWN
    }
}
