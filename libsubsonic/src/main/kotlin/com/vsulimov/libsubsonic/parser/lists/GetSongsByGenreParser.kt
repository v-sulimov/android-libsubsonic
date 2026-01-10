package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.response.lists.SongsByGenreResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getSongsByGenre` response payload.
 */
internal object GetSongsByGenreParser {

    /**
     * Parses the "subsonic-response" object into a [SongsByGenreResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [SongsByGenreResponse].
     */
    fun parse(json: JSONObject): SongsByGenreResponse {
        val songs = json.optJSONObject("songsByGenre")
            ?.parseList("song") { GetSongParser.parseSong(it) }
            ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return SongsByGenreResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            songs = songs
        )
    }
}
