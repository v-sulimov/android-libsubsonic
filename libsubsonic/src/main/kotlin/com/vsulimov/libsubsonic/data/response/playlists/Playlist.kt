package com.vsulimov.libsubsonic.data.response.playlists

import com.vsulimov.libsubsonic.data.response.browsing.Child

/**
 * Represents a playlist on the Subsonic server.
 *
 * @property id The unique identifier for the playlist.
 * @property name The display name of the playlist.
 * @property comment An optional comment or description for the playlist.
 * @property owner The username of the playlist owner.
 * @property public Indicates whether the playlist is publicly accessible.
 * @property songCount The number of songs in the playlist.
 * @property duration The total duration of the playlist in seconds.
 * @property created The ISO 8601 timestamp indicating when the playlist was created.
 * @property coverArt The identifier for the cover art associated with this playlist.
 * @property allowedUsers The list of usernames that are allowed to access this playlist.
 * @property entries The list of [Child] items (songs) contained in this playlist.
 */
data class Playlist(
    val id: String,
    val name: String,
    val comment: String? = null,
    val owner: String? = null,
    val public: Boolean = false,
    val songCount: Int = 0,
    val duration: Int = 0,
    val created: String? = null,
    val coverArt: String? = null,
    val allowedUsers: List<String> = emptyList(),
    val entries: List<Child> = emptyList()
)
