package com.vsulimov.libsubsonic.data.response.playlists

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getPlaylist` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property playlist The requested playlist including its song entries.
 */
data class PlaylistResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val playlist: Playlist
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
