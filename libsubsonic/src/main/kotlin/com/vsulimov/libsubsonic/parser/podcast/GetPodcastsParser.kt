package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.PodcastChannel
import com.vsulimov.libsubsonic.data.response.podcast.PodcastEpisode
import com.vsulimov.libsubsonic.data.response.podcast.PodcastsResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getPodcasts` response payload.
 */
internal object GetPodcastsParser {

    /**
     * Parses the "subsonic-response" object into a [PodcastsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [PodcastsResponse].
     */
    fun parse(json: JSONObject): PodcastsResponse {
        val podcastsObj = json.optJSONObject("podcasts")
        val channels = podcastsObj?.parseList("channel") { channelJson ->
            PodcastChannel(
                id = channelJson.optString("id"),
                url = channelJson.optString("url"),
                title = channelJson.optString("title").ifEmpty { null },
                description = channelJson.optString("description").ifEmpty { null },
                coverArt = channelJson.optString("coverArt").ifEmpty { null },
                originalImageUrl = channelJson.optString("originalImageUrl").ifEmpty { null },
                status = channelJson.optString("status"),
                errorMessage = channelJson.optString("errorMessage").ifEmpty { null },
                episodes = channelJson.parseList("episode") { episodeJson ->
                    parseEpisode(episodeJson)
                }
            )
        } ?: emptyList()

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

    /**
     * Parses a podcast episode JSON object into a [PodcastEpisode].
     *
     * This function is shared with [GetNewestPodcastsParser] which reuses it to parse
     * individual episode entries from the newest-episodes list.
     *
     * @param json The JSON object representing a podcast episode.
     * @return The parsed [PodcastEpisode].
     */
    internal fun parseEpisode(json: JSONObject): PodcastEpisode = PodcastEpisode(
        id = json.optString("id"),
        streamId = json.optString("streamId").ifEmpty { null },
        channelId = json.optString("channelId"),
        title = json.optString("title"),
        description = json.optString("description").ifEmpty { null },
        publishDate = json.optString("publishDate").ifEmpty { null },
        status = json.optString("status"),
        parent = json.optString("parent").ifEmpty { null },
        isDir = json.optBoolean("isDir", false),
        album = json.optString("album").ifEmpty { null },
        artist = json.optString("artist").ifEmpty { null },
        artistId = json.optString("artistId").ifEmpty { null },
        year = if (json.has("year")) json.optInt("year") else null,
        genre = json.optString("genre").ifEmpty { null },
        coverArt = json.optString("coverArt").ifEmpty { null },
        size = if (json.has("size")) json.optLong("size") else null,
        contentType = json.optString("contentType").ifEmpty { null },
        suffix = json.optString("suffix").ifEmpty { null },
        duration = if (json.has("duration")) json.optInt("duration") else null,
        bitRate = if (json.has("bitRate")) json.optInt("bitRate") else null,
        isVideo = json.optBoolean("isVideo", false),
        created = json.optString("created").ifEmpty { null },
        type = json.optString("type").ifEmpty { null },
        path = json.optString("path").ifEmpty { null }
    )
}
