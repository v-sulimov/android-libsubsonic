package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.DeletePodcastChannelResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `deletePodcastChannel` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object DeletePodcastChannelParser {

    /**
     * Parses the "subsonic-response" object into a [DeletePodcastChannelResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [DeletePodcastChannelResponse].
     */
    fun parse(json: JSONObject): DeletePodcastChannelResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return DeletePodcastChannelResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
