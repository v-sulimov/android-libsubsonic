package com.vsulimov.libsubsonic.data.response.browsing

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getMusicFolders` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property musicFolders The list of configured music folders on the server.
 */
data class MusicFoldersResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val musicFolders: List<MusicFolder>
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
