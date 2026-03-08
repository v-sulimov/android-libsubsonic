package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.TopSongsResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getTopSongs` response payload.
 */
internal object GetTopSongsParser {

    /**
     * Parses the "subsonic-response" object into a [TopSongsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [TopSongsResponse].
     */
    fun parse(json: JSONObject): TopSongsResponse {
        val songs = json.optJSONObject("topSongs")
            ?.parseList("song") { GetSongParser.parseSong(it) }
            ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return TopSongsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            songs = songs
        )
    }
}
