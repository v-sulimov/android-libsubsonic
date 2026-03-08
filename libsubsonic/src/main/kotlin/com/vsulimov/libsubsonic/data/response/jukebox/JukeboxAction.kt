package com.vsulimov.libsubsonic.data.response.jukebox

/**
 * Represents the action performed by the `jukeboxControl` endpoint.
 *
 * @property value The string value sent to the Subsonic server as the `action` parameter.
 */
enum class JukeboxAction(val value: String) {

    /** Returns the current playlist and playback status. */
    GET("get"),

    /** Returns the current playback status without playlist entries. */
    STATUS("status"),

    /** Replaces the current playlist with the specified songs. */
    SET("set"),

    /** Starts playback. */
    START("start"),

    /** Stops playback. */
    STOP("stop"),

    /** Skips to the specified playlist index. */
    SKIP("skip"),

    /** Adds songs to the end of the playlist. */
    ADD("add"),

    /** Clears the playlist. */
    CLEAR("clear"),

    /** Removes the song at the specified index from the playlist. */
    REMOVE("remove"),

    /** Shuffles the playlist. */
    SHUFFLE("shuffle"),

    /** Sets the playback gain (0.0 to 1.0). */
    SET_GAIN("setGain")
}
