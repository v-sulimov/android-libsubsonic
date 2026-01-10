package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents an album in the Subsonic library with detailed ID3-based metadata.
 *
 * This model captures comprehensive information about an album, including its
 * association with an artist, playback statistics, and categorization.
 *
 * @property id The unique identifier for the album.
 * @property name The name of the album.
 * @property artist The name of the artist associated with this album.
 * @property artistId The unique identifier for the artist.
 * @property coverArt The identifier for the cover art associated with this album.
 * @property songCount The total number of songs contained in this album.
 * @property duration The total duration of the album in seconds.
 * @property playCount The number of times this album has been played.
 * @property created The ISO 8601 timestamp indicating when the album was added to the library.
 * @property year The release year of the album.
 * @property genre The primary musical genre of the album.
 * @property played The ISO 8601 timestamp indicating when the album was last played.
 * @property starred The ISO 8601 timestamp indicating when this album was starred by the user.
 * @property userRating The rating given to the album by the current user (typically 1-5).
 * @property musicBrainzId The MusicBrainz identifier for the album release.
 * @property isCompilation Indicates whether the album is a compilation of various artists.
 * @property sortName The name used for sorting purposes (for example, removing leading articles).
 * @property displayArtist The artist name formatted for display.
 * @property explicitStatus The content advisory status (for example, "explicit" or "clean").
 * @property songs The list of [Child] items (songs) contained in this album.
 */
data class Album(
    val id: String,
    val name: String,
    val artist: String? = null,
    val artistId: String? = null,
    val coverArt: String? = null,
    val songCount: Int? = null,
    val duration: Int? = null,
    val playCount: Int? = null,
    val created: String? = null,
    val year: Int? = null,
    val genre: String? = null,
    val played: String? = null,
    val starred: String? = null,
    val userRating: Int? = null,
    val musicBrainzId: String? = null,
    val isCompilation: Boolean = false,
    val sortName: String? = null,
    val displayArtist: String? = null,
    val explicitStatus: String? = null,
    val songs: List<Child> = emptyList()
)
