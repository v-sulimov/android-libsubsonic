package com.vsulimov.libsubsonic.data.response.user

/**
 * Represents a user account returned by the server.
 *
 * @property username The username of the user.
 * @property email The email address of the user, if provided.
 * @property scrobblingEnabled Whether Last.fm scrobbling is enabled for this user.
 * @property adminRole Whether the user has administrator privileges.
 * @property settingsRole Whether the user is allowed to change personal settings and password.
 * @property downloadRole Whether the user is allowed to download files.
 * @property uploadRole Whether the user is allowed to upload files.
 * @property playlistRole Whether the user is allowed to create and delete playlists.
 * @property coverArtRole Whether the user is allowed to change cover art and tags.
 * @property commentRole Whether the user is allowed to create and edit comments and ratings.
 * @property podcastRole Whether the user is allowed to administrate podcasts.
 * @property streamRole Whether the user is allowed to play files.
 * @property jukeboxRole Whether the user is allowed to play files in jukebox mode.
 * @property shareRole Whether the user is allowed to share files with anyone.
 * @property folders The IDs of the music folders the user has access to.
 */
data class User(
    val username: String,
    val email: String?,
    val scrobblingEnabled: Boolean,
    val adminRole: Boolean,
    val settingsRole: Boolean,
    val downloadRole: Boolean,
    val uploadRole: Boolean,
    val playlistRole: Boolean,
    val coverArtRole: Boolean,
    val commentRole: Boolean,
    val podcastRole: Boolean,
    val streamRole: Boolean,
    val jukeboxRole: Boolean,
    val shareRole: Boolean,
    val folders: List<Int>
)
