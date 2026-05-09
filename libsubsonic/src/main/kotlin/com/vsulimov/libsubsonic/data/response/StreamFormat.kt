package com.vsulimov.libsubsonic.data.response

/**
 * Target format for the `stream` endpoint, sent as the `format` query parameter.
 *
 * The Subsonic spec defines [Raw] as the only universal value — every other format corresponds
 * to a transcoder configured on the server, the names of which are server-specific. Use [Custom]
 * to pass any of those server-defined transcoder names through unchanged.
 */
sealed class StreamFormat {

    /** The wire-format string sent to the Subsonic server. */
    abstract val value: String

    /** Skip server-side transcoding and stream the original file. */
    object Raw : StreamFormat() {
        override val value: String = "raw"
    }

    /**
     * A server-configured transcoder name (for example `"mp3"`, `"opus"`, `"ogg"`).
     *
     * @property value The transcoder name as configured on the Subsonic server.
     */
    data class Custom(override val value: String) : StreamFormat()
}
