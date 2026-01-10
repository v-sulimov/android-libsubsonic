package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents a shortcut entry within the indexes response.
 *
 * @property id The unique identifier for the shortcut.
 * @property name The display name of the shortcut.
 */
data class Shortcut(val id: String, val name: String)
