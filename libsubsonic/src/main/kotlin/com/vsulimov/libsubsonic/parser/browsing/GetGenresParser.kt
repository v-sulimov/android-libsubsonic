package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Genre
import com.vsulimov.libsubsonic.data.response.browsing.GenresResponse
import org.json.JSONObject

/**
 * Internal parser responsible for extracting [GenresResponse] from the JSON response.
 */
internal object GetGenresParser {

    /**
     * Extracts genres and metadata from the "subsonic-response" object.
     *
     * @param json The "subsonic-response" JSONObject.
     * @return A [GenresResponse] containing metadata and the list of genres.
     */
    fun parse(json: JSONObject): GenresResponse {
        val genresObj = json.optJSONObject("genres")
        val result = mutableListOf<Genre>()

        if (genresObj != null) {
            val genreArray = genresObj.optJSONArray("genre")
            if (genreArray != null) {
                for (i in 0 until genreArray.length()) {
                    result.add(parseSingleGenre(json = genreArray.getJSONObject(i)))
                }
            } else {
                genresObj.optJSONObject("genre")?.let {
                    result.add(parseSingleGenre(json = it))
                }
            }
        }

        return GenresResponse(
            status = json.optString("status", "ok"),
            apiVersion = json.optString("version", "Unknown"),
            serverType = json.optString("type").ifEmpty { null },
            serverVersion = json.optString("serverVersion").ifEmpty { null },
            isOpenSubsonic = json.optBoolean("openSubsonic", false),
            genres = result
        )
    }

    private fun parseSingleGenre(json: JSONObject): Genre = Genre(
        value = json.optString("value"),
        songCount = json.optInt("songCount", 0),
        albumCount = json.optInt("albumCount", 0)
    )
}
