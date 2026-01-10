package com.vsulimov.libsubsonic.data.response.lists

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getNowPlaying` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property entries The list of currently playing entries across all active users.
 */
data class NowPlayingResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val entries: List<NowPlayingEntry>
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
