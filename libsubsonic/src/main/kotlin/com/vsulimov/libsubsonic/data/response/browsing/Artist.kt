package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents an artist in the Subsonic library with comprehensive metadata.
 *
 * This unified model is used across multiple API endpoints to represent artist data,
 * ranging from basic list entries to detailed artist profiles including album lists.
 *
 * @property id The unique identifier for the artist.
 * @property name The display name of the artist.
 * @property albumCount The total number of albums associated with this artist.
 * @property coverArt The identifier for the cover art associated with this artist.
 * @property artistImageUrl The URL for the artist's representative image.
 * @property musicBrainzId The MusicBrainz identifier for the artist.
 * @property sortName The name used for sorting purposes (e.g., removing leading articles).
 * @property roles The list of roles assigned to this artist (e.g., "artist", "albumartist").
 * @property albums The list of [Album]s associated with this artist (present in detailed responses).
 */
data class Artist(
    val id: String,
    val name: String,
    val albumCount: Int = 0,
    val coverArt: String? = null,
    val artistImageUrl: String? = null,
    val musicBrainzId: String? = null,
    val sortName: String? = null,
    val roles: List<String> = emptyList(),
    val albums: List<Album> = emptyList()
)
