package com.vsulimov.libsubsonic.data.response.podcast

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getNewestPodcasts` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property episodes The list of newest podcast episodes returned by the server.
 */
data class NewestPodcastsResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val episodes: List<PodcastEpisode>
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
