package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents a configured music folder on the Subsonic server.
 *
 * @property id The unique ID of the folder (used for indexes/searching).
 * @property name The user-friendly name (e.g., "Music", "Audiobooks").
 */
data class MusicFolder(val id: String, val name: String)
