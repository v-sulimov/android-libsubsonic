package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents a directory in the music library, containing sub-directories and songs.
 *
 * @property id The unique identifier for this directory.
 * @property name The display name of the directory.
 * @property playCount The number of times this directory (or its contents) has been played.
 * @property played The date when this directory was last played.
 * @property albumCount The number of albums contained within this directory (if applicable).
 * @property children The list of items (folders or songs) contained within.
 */
data class Directory(
    val id: String,
    val name: String,
    val playCount: Int? = null,
    val played: String? = null,
    val albumCount: Int? = null,
    val children: List<Child>
)
