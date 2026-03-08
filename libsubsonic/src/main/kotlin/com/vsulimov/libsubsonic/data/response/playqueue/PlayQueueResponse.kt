package com.vsulimov.libsubsonic.data.response.playqueue

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getPlayQueue` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property playQueue The user's play queue, or null if none exists.
 */
data class PlayQueueResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val playQueue: PlayQueue?
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
