package com.vsulimov.libsubsonic.parser.podcast

import com.vsulimov.libsubsonic.data.response.podcast.DownloadPodcastEpisodeResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `downloadPodcastEpisode` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object DownloadPodcastEpisodeParser {

    /**
     * Parses the `subsonic-response` object into a [DownloadPodcastEpisodeResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [DownloadPodcastEpisodeResponse].
     */
    fun parse(json: JSONObject): DownloadPodcastEpisodeResponse = json.envelopeOnly(::DownloadPodcastEpisodeResponse)
}
