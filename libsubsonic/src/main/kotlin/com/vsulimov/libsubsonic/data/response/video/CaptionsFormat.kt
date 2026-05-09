package com.vsulimov.libsubsonic.data.response.video

/**
 * Captions file format requested from the `getCaptions` endpoint.
 *
 * @property value The string sent to the Subsonic server as the `format` parameter.
 */
enum class CaptionsFormat(val value: String) {

    /** SubRip subtitle format. */
    SRT("srt"),

    /** WebVTT subtitle format. */
    VTT("vtt")
}
