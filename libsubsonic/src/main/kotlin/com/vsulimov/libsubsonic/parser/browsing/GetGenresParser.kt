package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Genre
import com.vsulimov.libsubsonic.data.response.browsing.GenresResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getGenres` response payload.
 */
internal object GetGenresParser {

    /**
     * Extracts genres and metadata from the "subsonic-response" object.
     *
     * @param json The "subsonic-response" JSONObject.
     * @return The parsed [GenresResponse].
     */
    fun parse(json: JSONObject): GenresResponse {
        val genres = json.optJSONObject("genres")
            ?.parseList("genre") { parseGenre(it) }
            ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return GenresResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            genres = genres
        )
    }

    private fun parseGenre(json: JSONObject): Genre {
        val value = json.optString("value").ifEmpty { json.optString("name") }
        return Genre(
            value = value,
            songCount = json.optInt("songCount", 0),
            albumCount = json.optInt("albumCount", 0)
        )
    }
}
