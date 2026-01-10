package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents a directory in the music library, containing sub-directories and songs.
 *
 * @property id The unique identifier for this directory.
 * @property parent The identifier of the parent directory, if available.
 * @property name The display name of the directory.
 * @property starred The ISO 8601 timestamp indicating when the directory was starred.
 * @property playCount The number of times this directory (or its contents) has been played.
 * @property played The ISO 8601 timestamp indicating when this directory was last played.
 * @property albumCount The number of albums contained within this directory, if applicable.
 * @property children The list of items (folders or songs) contained within.
 */
data class Directory(
    val id: String,
    val parent: String? = null,
    val name: String,
    val starred: String? = null,
    val playCount: Int? = null,
    val played: String? = null,
    val albumCount: Int? = null,
    val children: List<Child>
)
