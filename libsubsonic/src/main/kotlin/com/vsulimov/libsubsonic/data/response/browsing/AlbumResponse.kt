package com.vsulimov.libsubsonic.data.response.browsing

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getAlbum` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property album The requested album details and its songs.
 */
data class AlbumResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val album: Album
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
