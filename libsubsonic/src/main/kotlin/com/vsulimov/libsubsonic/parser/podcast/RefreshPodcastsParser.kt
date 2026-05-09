package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.RefreshPodcastsResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `refreshPodcasts` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object RefreshPodcastsParser {

    /**
     * Parses the `subsonic-response` object into a [RefreshPodcastsResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [RefreshPodcastsResponse].
     */
    fun parse(json: JSONObject): RefreshPodcastsResponse = json.envelopeOnly(::RefreshPodcastsResponse)
}
