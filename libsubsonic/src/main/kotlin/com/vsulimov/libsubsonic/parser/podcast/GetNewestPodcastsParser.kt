package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.NewestPodcastsResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getNewestPodcasts` response payload.
 */
internal object GetNewestPodcastsParser {

    /**
     * Parses the `subsonic-response` object into a [NewestPodcastsResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [NewestPodcastsResponse].
     */
    fun parse(json: JSONObject): NewestPodcastsResponse {
        val episodes = json.optJSONObject("newestPodcasts")
            ?.parseList("episode", GetPodcastsParser::parseEpisode)
            .orEmpty()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return NewestPodcastsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            episodes = episodes
        )
    }
}
