package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.SimilarSongsResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getSimilarSongs2` response payload.
 */
internal object GetSimilarSongsParser {

    /**
     * Parses the "subsonic-response" object into a [SimilarSongsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [SimilarSongsResponse].
     */
    fun parse(json: JSONObject): SimilarSongsResponse {
        val songs = json.optJSONObject("similarSongs2")
            ?.parseList("song") { GetSongParser.parseSong(it) }
            ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return SimilarSongsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            songs = songs
        )
    }
}
