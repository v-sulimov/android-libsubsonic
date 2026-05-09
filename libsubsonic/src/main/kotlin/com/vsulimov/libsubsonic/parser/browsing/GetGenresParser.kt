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
     * Parses the `subsonic-response` object into a [GenresResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [GenresResponse].
     */
    fun parse(json: JSONObject): GenresResponse {
        val genres = json.optJSONObject("genres")?.parseList("genre", ::parseGenre).orEmpty()

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

    /**
     * Parses a single `genre` JSON object into a [Genre].
     *
     * Tolerates both the legacy `value` field and the OpenSubsonic `name` field, preferring
     * `value` when both are present.
     *
     * @param json The JSON object representing a genre.
     * @return The parsed [Genre].
     */
    private fun parseGenre(json: JSONObject) = Genre(
        name = json.optString("value").ifEmpty { json.optString("name") },
        songCount = json.optInt("songCount", 0),
        albumCount = json.optInt("albumCount", 0)
    )
}
