package com.vsulimov.libsubsonic.data.response.browsing

/**
 * A simplified artist entry returned within an index.
 *
 * @property id The unique identifier for the artist/directory.
 * @property name The name of the artist or directory.
 * @property coverArt The ID of the cover art for this artist.
 * @property artistImageUrl Optional URL for the artist's representative image.
 */
data class ArtistEntry(
    val id: String,
    val name: String,
    val coverArt: String? = null,
    val artistImageUrl: String? = null
)
