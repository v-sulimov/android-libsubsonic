package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.ArtistEntry
import com.vsulimov.libsubsonic.data.response.browsing.Index
import com.vsulimov.libsubsonic.data.response.browsing.IndexesResponse
import com.vsulimov.libsubsonic.data.response.browsing.Shortcut
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getIndexes` response payload.
 */
internal object GetIndexesParser {

    /**
     * Parses the `subsonic-response` object into an [IndexesResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [IndexesResponse].
     */
    fun parse(json: JSONObject): IndexesResponse {
        val indexesObj = json.optJSONObject("indexes")
        val lastModified = indexesObj?.optLong("lastModified", 0L) ?: 0L
        val ignoredArticles = indexesObj?.optString("ignoredArticles", "") ?: ""
        val shortcuts = indexesObj?.parseList("shortcut", ::parseShortcut).orEmpty()
        val indexes = indexesObj?.parseList("index", ::parseIndexObject).orEmpty()
        val children = indexesObj?.parseList("child", GetSongParser::parseSong).orEmpty()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return IndexesResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            lastModified = lastModified,
            ignoredArticles = ignoredArticles,
            shortcuts = shortcuts,
            indexes = indexes,
            children = children
        )
    }

    private fun parseShortcut(json: JSONObject) = Shortcut(
        id = json.optString("id"),
        name = json.optString("name")
    )

    /**
     * Parses an alphabetical index group into an [Index].
     *
     * @param json The JSON object representing an index entry.
     * @return The parsed [Index], or `null` if the index has no name.
     */
    private fun parseIndexObject(json: JSONObject): Index? {
        val name = json.optString("name").ifEmpty { return null }
        return Index(name = name, artists = json.parseList("artist", ::parseArtistEntry))
    }

    private fun parseArtistEntry(json: JSONObject) = ArtistEntry(
        id = json.optString("id"),
        name = json.optString("name"),
        coverArt = json.optStringOrNull("coverArt"),
        artistImageUrl = json.optStringOrNull("artistImageUrl"),
        starred = json.optStringOrNull("starred")
    )
}
