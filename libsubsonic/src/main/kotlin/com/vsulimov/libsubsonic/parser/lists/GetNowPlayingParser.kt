package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.response.lists.NowPlayingEntry
import com.vsulimov.libsubsonic.data.response.lists.NowPlayingResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getNowPlaying` response payload.
 */
internal object GetNowPlayingParser {

    /**
     * Parses the `subsonic-response` object into a [NowPlayingResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [NowPlayingResponse].
     */
    fun parse(json: JSONObject): NowPlayingResponse {
        val entries = json.optJSONObject("nowPlaying")
            ?.parseList("entry", ::parseEntry)
            .orEmpty()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return NowPlayingResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            entries = entries
        )
    }

    private fun parseEntry(json: JSONObject) = NowPlayingEntry(
        username = json.optString("username"),
        minutesAgo = json.optInt("minutesAgo"),
        playerId = json.optInt("playerId"),
        playerName = json.optStringOrNull("playerName"),
        song = GetSongParser.parseSong(json)
    )
}
