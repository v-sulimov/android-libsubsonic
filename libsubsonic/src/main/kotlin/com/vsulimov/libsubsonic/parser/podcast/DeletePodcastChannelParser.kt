package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.DeletePodcastChannelResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `deletePodcastChannel` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object DeletePodcastChannelParser {

    /**
     * Parses the `subsonic-response` object into a [DeletePodcastChannelResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [DeletePodcastChannelResponse].
     */
    fun parse(json: JSONObject): DeletePodcastChannelResponse = json.envelopeOnly(::DeletePodcastChannelResponse)
}
