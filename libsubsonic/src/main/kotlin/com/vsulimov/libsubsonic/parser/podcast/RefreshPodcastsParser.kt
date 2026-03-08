package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.RefreshPodcastsResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `refreshPodcasts` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object RefreshPodcastsParser {

    /**
     * Parses the "subsonic-response" object into a [RefreshPodcastsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [RefreshPodcastsResponse].
     */
    fun parse(json: JSONObject): RefreshPodcastsResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return RefreshPodcastsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
