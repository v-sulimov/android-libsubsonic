package com.vsulimov.libsubsonic.data.response.browsing

/**
 * An alphabetical grouping of [Artist] objects.
 *
 * @property name The index letter (e.g., "A", "B").
 * @property artists The list of artists under this letter.
 */
data class ArtistIndex(val name: String, val artists: List<Artist>)
