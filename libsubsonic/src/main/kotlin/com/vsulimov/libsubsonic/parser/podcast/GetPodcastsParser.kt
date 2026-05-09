package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.PodcastChannel
import com.vsulimov.libsubsonic.data.response.podcast.PodcastEpisode
import com.vsulimov.libsubsonic.data.response.podcast.PodcastStatus
import com.vsulimov.libsubsonic.data.response.podcast.PodcastsResponse
import com.vsulimov.libsubsonic.parser.optIntOrNull
import com.vsulimov.libsubsonic.parser.optLongOrNull
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getPodcasts` response payload and provides the shared [parseEpisode] helper used
 * by [GetNewestPodcastsParser].
 */
internal object GetPodcastsParser {

    /**
     * Parses the `subsonic-response` object into a [PodcastsResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [PodcastsResponse].
     */
    fun parse(json: JSONObject): PodcastsResponse {
        val channels = json.optJSONObject("podcasts")
            ?.parseList("channel", ::parseChannel)
            .orEmpty()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return PodcastsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            channels = channels
        )
    }

    private fun parseChannel(json: JSONObject) = PodcastChannel(
        id = json.optString("id"),
        url = json.optString("url"),
        title = json.optStringOrNull("title"),
        description = json.optStringOrNull("description"),
        coverArt = json.optStringOrNull("coverArt"),
        originalImageUrl = json.optStringOrNull("originalImageUrl"),
        status = PodcastStatus.fromValue(json.optString("status")),
        errorMessage = json.optStringOrNull("errorMessage"),
        episodes = json.parseList("episode", ::parseEpisode)
    )

    /**
     * Parses a `podcastEpisode` JSON object into a [PodcastEpisode].
     *
     * Shared with [GetNewestPodcastsParser] for the newest-episodes list.
     *
     * @param json The JSON object representing a podcast episode.
     * @return The parsed [PodcastEpisode].
     */
    internal fun parseEpisode(json: JSONObject) = PodcastEpisode(
        id = json.optString("id"),
        streamId = json.optStringOrNull("streamId"),
        channelId = json.optString("channelId"),
        title = json.optString("title"),
        description = json.optStringOrNull("description"),
        publishDate = json.optStringOrNull("publishDate"),
        status = PodcastStatus.fromValue(json.optString("status")),
        parent = json.optStringOrNull("parent"),
        isDir = json.optBoolean("isDir", false),
        album = json.optStringOrNull("album"),
        artist = json.optStringOrNull("artist"),
        artistId = json.optStringOrNull("artistId"),
        year = json.optIntOrNull("year"),
        genre = json.optStringOrNull("genre"),
        coverArt = json.optStringOrNull("coverArt"),
        size = json.optLongOrNull("size"),
        contentType = json.optStringOrNull("contentType"),
        suffix = json.optStringOrNull("suffix"),
        duration = json.optIntOrNull("duration"),
        bitRate = json.optIntOrNull("bitRate"),
        isVideo = json.optBoolean("isVideo", false),
        created = json.optStringOrNull("created"),
        type = json.optStringOrNull("type"),
        path = json.optStringOrNull("path")
    )
}
