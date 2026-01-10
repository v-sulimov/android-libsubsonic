package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents extended album metadata returned by the Subsonic API.
 *
 * @property notes Free-form notes or description for the album.
 * @property musicBrainzId The MusicBrainz identifier for the album release.
 * @property lastFmUrl The Last.fm URL for the album.
 * @property smallImageUrl The URL for a small album image.
 * @property mediumImageUrl The URL for a medium album image.
 * @property largeImageUrl The URL for a large album image.
 */
data class AlbumInfo(
    val notes: String? = null,
    val musicBrainzId: String? = null,
    val lastFmUrl: String? = null,
    val smallImageUrl: String? = null,
    val mediumImageUrl: String? = null,
    val largeImageUrl: String? = null
)
