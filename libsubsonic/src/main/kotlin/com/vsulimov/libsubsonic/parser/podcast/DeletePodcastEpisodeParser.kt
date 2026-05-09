package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.DeletePodcastEpisodeResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `deletePodcastEpisode` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object DeletePodcastEpisodeParser {

    /**
     * Parses the `subsonic-response` object into a [DeletePodcastEpisodeResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [DeletePodcastEpisodeResponse].
     */
    fun parse(json: JSONObject): DeletePodcastEpisodeResponse = json.envelopeOnly(::DeletePodcastEpisodeResponse)
}
