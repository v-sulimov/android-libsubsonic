package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents an alphabetical or category index containing a list of artists.
 *
 * @property name The name of the index (for example, "A", "B", or "Various").
 * @property artists The list of artists belonging to this index.
 */
data class Index(val name: String, val artists: List<ArtistEntry>)
