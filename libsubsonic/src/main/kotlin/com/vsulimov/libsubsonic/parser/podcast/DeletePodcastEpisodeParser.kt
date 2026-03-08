package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.DeletePodcastEpisodeResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `deletePodcastEpisode` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object DeletePodcastEpisodeParser {

    /**
     * Parses the "subsonic-response" object into a [DeletePodcastEpisodeResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [DeletePodcastEpisodeResponse].
     */
    fun parse(json: JSONObject): DeletePodcastEpisodeResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return DeletePodcastEpisodeResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
