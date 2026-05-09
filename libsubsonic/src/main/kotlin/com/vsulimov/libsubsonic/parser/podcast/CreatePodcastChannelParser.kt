package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.CreatePodcastChannelResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `createPodcastChannel` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object CreatePodcastChannelParser {

    /**
     * Parses the `subsonic-response` object into a [CreatePodcastChannelResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [CreatePodcastChannelResponse].
     */
    fun parse(json: JSONObject): CreatePodcastChannelResponse = json.envelopeOnly(::CreatePodcastChannelResponse)
}
