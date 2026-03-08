package com.vsulimov.libsubsonic.data.response.video

import com.vsulimov.libsubsonic.data.response.SubsonicResponse
import com.vsulimov.libsubsonic.data.response.browsing.Child

/**
 * Represents the response from a `getVideos` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property videos The list of video items available on the server.
 */
data class VideosResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val videos: List<Child>
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
