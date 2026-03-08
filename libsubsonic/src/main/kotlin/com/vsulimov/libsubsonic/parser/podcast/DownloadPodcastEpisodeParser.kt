package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.DownloadPodcastEpisodeResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `downloadPodcastEpisode` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object DownloadPodcastEpisodeParser {

    /**
     * Parses the "subsonic-response" object into a [DownloadPodcastEpisodeResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [DownloadPodcastEpisodeResponse].
     */
    fun parse(json: JSONObject): DownloadPodcastEpisodeResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return DownloadPodcastEpisodeResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
