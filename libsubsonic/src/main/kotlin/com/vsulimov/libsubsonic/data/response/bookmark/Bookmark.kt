package com.vsulimov.libsubsonic.data.response.bookmark

import com.vsulimov.libsubsonic.data.response.browsing.Child

/**
 * Represents a playback bookmark saved by a user.
 *
 * @property position The position in milliseconds within the entry where playback was paused.
 * @property username The username of the user who created the bookmark.
 * @property comment An optional comment associated with the bookmark.
 * @property created The date and time the bookmark was created.
 * @property changed The date and time the bookmark was last modified.
 * @property entry The media entry the bookmark refers to.
 */
data class Bookmark(
    val position: Long,
    val username: String,
    val comment: String?,
    val created: String,
    val changed: String,
    val entry: Child
)
