package com.vsulimov.libsubsonic.data.response.video

/**
 * Represents detailed information about a video, including its available captions,
 * audio tracks, and conversion profiles.
 *
 * @property id The unique identifier of the video.
 * @property captions The list of captions tracks available for this video.
 * @property audioTracks The list of audio tracks available for this video.
 * @property conversions The list of conversion profiles available for this video.
 */
data class VideoInfo(
    val id: String,
    val captions: List<Caption> = emptyList(),
    val audioTracks: List<AudioTrack> = emptyList(),
    val conversions: List<Conversion> = emptyList()
)
