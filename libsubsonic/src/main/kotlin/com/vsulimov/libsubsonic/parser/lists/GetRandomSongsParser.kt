package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.response.lists.RandomSongsResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getRandomSongs` response payload.
 */
internal object GetRandomSongsParser {

    /**
     * Parses the "subsonic-response" object into a [RandomSongsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [RandomSongsResponse].
     */
    fun parse(json: JSONObject): RandomSongsResponse {
        val songs = json.optJSONObject("randomSongs")
            ?.parseList("song") { GetSongParser.parseSong(it) }
            ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return RandomSongsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            songs = songs
        )
    }
}
