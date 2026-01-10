package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents a musical genre as categorized by the Subsonic server.
 *
 * @property value The name of the genre (for example, "Rock" or "Electronic").
 * @property songCount The total number of songs associated with this genre.
 * @property albumCount The total number of albums associated with this genre.
 */
data class Genre(val value: String, val songCount: Int, val albumCount: Int)
