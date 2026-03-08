package com.vsulimov.libsubsonic.data.response.podcast

/**
 * Represents a single podcast episode returned by the Subsonic server.
 *
 * @property id The unique identifier of the episode.
 * @property streamId The identifier used to stream this episode, if available.
 * @property channelId The identifier of the channel this episode belongs to.
 * @property title The title of the episode.
 * @property description The description of the episode.
 * @property publishDate The ISO 8601 timestamp indicating when the episode was published.
 * @property status The download status of the episode (e.g., "completed", "downloading", "error").
 * @property parent The identifier of the parent directory.
 * @property isDir True if this item is a directory.
 * @property album The album name associated with this episode.
 * @property artist The artist name associated with this episode.
 * @property artistId The identifier of the artist associated with this episode.
 * @property year The release year.
 * @property genre The genre of the episode.
 * @property coverArt The identifier for the cover art.
 * @property size The file size in bytes.
 * @property contentType The MIME type (e.g., "audio/mpeg").
 * @property suffix The file extension (e.g., "mp3").
 * @property duration The duration in seconds.
 * @property bitRate The bit rate in kilobits per second.
 * @property isVideo True if this episode is a video file.
 * @property created The ISO 8601 timestamp indicating when this episode was added to the library.
 * @property type The Subsonic item type (e.g., "podcast").
 * @property path The file path on the server.
 */
data class PodcastEpisode(
    val id: String,
    val streamId: String? = null,
    val channelId: String,
    val title: String,
    val description: String? = null,
    val publishDate: String? = null,
    val status: String,
    val parent: String? = null,
    val isDir: Boolean = false,
    val album: String? = null,
    val artist: String? = null,
    val artistId: String? = null,
    val year: Int? = null,
    val genre: String? = null,
    val coverArt: String? = null,
    val size: Long? = null,
    val contentType: String? = null,
    val suffix: String? = null,
    val duration: Int? = null,
    val bitRate: Int? = null,
    val isVideo: Boolean = false,
    val created: String? = null,
    val type: String? = null,
    val path: String? = null
)
