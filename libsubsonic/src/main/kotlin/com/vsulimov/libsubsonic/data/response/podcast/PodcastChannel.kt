package com.vsulimov.libsubsonic.data.response.podcast

/**
 * Represents a podcast channel returned by the Subsonic server.
 *
 * @property id The unique identifier of the channel.
 * @property url The RSS feed URL of the channel.
 * @property title The title of the channel.
 * @property description The description of the channel.
 * @property coverArt The identifier for the cover art.
 * @property originalImageUrl The original image URL from the RSS feed.
 * @property status The status of the channel (e.g., "completed", "error").
 * @property errorMessage The error message if the channel status is "error".
 * @property episodes The list of episodes in this channel.
 */
data class PodcastChannel(
    val id: String,
    val url: String,
    val title: String? = null,
    val description: String? = null,
    val coverArt: String? = null,
    val originalImageUrl: String? = null,
    val status: String,
    val errorMessage: String? = null,
    val episodes: List<PodcastEpisode> = emptyList()
)
