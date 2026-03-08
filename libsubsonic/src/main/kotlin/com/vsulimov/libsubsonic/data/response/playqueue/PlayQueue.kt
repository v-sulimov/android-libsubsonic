package com.vsulimov.libsubsonic.data.response.playqueue

import com.vsulimov.libsubsonic.data.response.browsing.Child

/**
 * Represents the server-side play queue for a user.
 *
 * @property current The ID of the currently playing entry, if any.
 * @property position The position in milliseconds within the current entry, if any.
 * @property username The username of the owner of the play queue.
 * @property changed The date and time the play queue was last changed.
 * @property changedBy The name of the client that last changed the play queue.
 * @property entries The list of media entries in the play queue.
 */
data class PlayQueue(
    val current: String?,
    val position: Long?,
    val username: String,
    val changed: String,
    val changedBy: String,
    val entries: List<Child>
)
