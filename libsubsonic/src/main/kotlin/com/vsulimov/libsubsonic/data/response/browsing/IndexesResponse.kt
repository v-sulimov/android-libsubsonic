package com.vsulimov.libsubsonic.data.response.browsing

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getIndexes` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property lastModified The timestamp (in milliseconds) of when the library was last modified.
 * @property ignoredArticles A space-separated list of articles (e.g., "The", "A") that are ignored during sorting.
 * @property shortcuts The list of shortcut entries.
 * @property indexes The list of alphabetical indexes.
 * @property children The list of child entries returned by the server.
 */
data class IndexesResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val lastModified: Long,
    val ignoredArticles: String,
    val shortcuts: List<Shortcut>,
    val indexes: List<Index>,
    val children: List<Child>
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
