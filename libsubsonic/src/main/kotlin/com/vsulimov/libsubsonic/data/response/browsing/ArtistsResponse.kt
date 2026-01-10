package com.vsulimov.libsubsonic.data.response.browsing

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getArtists` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property ignoredArticles A space-separated list of articles that are ignored during sorting.
 * @property artists The list of artists grouped by alphabetical index.
 */
data class ArtistsResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val ignoredArticles: String,
    val artists: List<ArtistIndex>
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
