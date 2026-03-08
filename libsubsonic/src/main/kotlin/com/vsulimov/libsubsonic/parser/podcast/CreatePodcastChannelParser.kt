package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.CreatePodcastChannelResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `createPodcastChannel` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object CreatePodcastChannelParser {

    /**
     * Parses the "subsonic-response" object into a [CreatePodcastChannelResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [CreatePodcastChannelResponse].
     */
    fun parse(json: JSONObject): CreatePodcastChannelResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return CreatePodcastChannelResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
