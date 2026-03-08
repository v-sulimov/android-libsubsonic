package com.vsulimov.libsubsonic.data.response.sharing

import com.vsulimov.libsubsonic.data.response.browsing.Child

/**
 * Represents a share created on the Subsonic server.
 *
 * @property id The unique identifier of the share.
 * @property url The public URL of the share.
 * @property description An optional description of the share.
 * @property username The username of the user who created the share.
 * @property created The ISO 8601 timestamp indicating when the share was created.
 * @property lastVisited The ISO 8601 timestamp indicating when the share was last visited.
 * @property expires The ISO 8601 timestamp indicating when the share expires.
 * @property visitCount The number of times the share has been visited.
 * @property entries The list of media entries included in the share.
 */
data class Share(
    val id: String,
    val url: String,
    val description: String? = null,
    val username: String,
    val created: String,
    val lastVisited: String? = null,
    val expires: String? = null,
    val visitCount: Int,
    val entries: List<Child> = emptyList()
)
