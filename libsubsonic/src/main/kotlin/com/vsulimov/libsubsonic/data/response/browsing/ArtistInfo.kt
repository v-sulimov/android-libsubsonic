package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents extended artist metadata returned by the Subsonic API.
 *
 * @property biography The artist biography text, if available.
 * @property musicBrainzId The MusicBrainz identifier for the artist.
 * @property lastFmUrl The Last.fm URL for the artist.
 * @property smallImageUrl The URL for a small artist image.
 * @property mediumImageUrl The URL for a medium artist image.
 * @property largeImageUrl The URL for a large artist image.
 * @property similarArtists List of similar artists suggested by the server.
 */
data class ArtistInfo(
    val biography: String? = null,
    val musicBrainzId: String? = null,
    val lastFmUrl: String? = null,
    val smallImageUrl: String? = null,
    val mediumImageUrl: String? = null,
    val largeImageUrl: String? = null,
    val similarArtists: List<Artist> = emptyList()
)
