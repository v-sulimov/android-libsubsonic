package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents a configured music folder on the Subsonic server.
 *
 * Music folders are top-level library roots configured on the server. They are referenced
 * by ID in browsing and search endpoints.
 *
 * @property id The unique identifier of the folder.
 * @property name The user-friendly name of the folder (for example, "Music" or "Audiobooks").
 */
data class MusicFolder(val id: String, val name: String)
