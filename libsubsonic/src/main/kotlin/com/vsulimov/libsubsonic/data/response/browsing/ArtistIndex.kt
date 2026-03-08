package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents an alphabetical grouping of [Artist] objects.
 *
 * @property name The index letter (for example, "A" or "B").
 * @property artists The list of artists under this letter.
 */
data class ArtistIndex(val name: String, val artists: List<Artist>)
