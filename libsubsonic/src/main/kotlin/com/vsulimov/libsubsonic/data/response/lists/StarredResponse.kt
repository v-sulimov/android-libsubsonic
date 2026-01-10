package com.vsulimov.libsubsonic.data.response.lists

import com.vsulimov.libsubsonic.data.response.SubsonicResponse
import com.vsulimov.libsubsonic.data.response.browsing.Album
import com.vsulimov.libsubsonic.data.response.browsing.Artist
import com.vsulimov.libsubsonic.data.response.browsing.Child

/**
 * Represents the response from a `getStarred` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property artists The list of starred artists.
 * @property albums The list of starred albums.
 * @property songs The list of starred songs.
 */
data class StarredResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val artists: List<Artist>,
    val albums: List<Album>,
    val songs: List<Child>
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
