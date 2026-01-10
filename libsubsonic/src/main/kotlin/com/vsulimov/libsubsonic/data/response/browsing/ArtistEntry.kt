package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents a simplified artist entry returned within an index.
 *
 * @property id The unique identifier for the artist or directory.
 * @property name The display name of the artist or directory.
 * @property coverArt The identifier for the cover art, if available.
 * @property artistImageUrl The URL for the artist's representative image, if available.
 * @property starred The ISO 8601 timestamp indicating when the artist was starred.
 */
data class ArtistEntry(
    val id: String,
    val name: String,
    val coverArt: String? = null,
    val artistImageUrl: String? = null,
    val starred: String? = null
)
