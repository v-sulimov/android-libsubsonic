package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.ArtistEntry
import com.vsulimov.libsubsonic.data.response.browsing.Index
import com.vsulimov.libsubsonic.data.response.browsing.IndexesResponse
import com.vsulimov.libsubsonic.data.response.browsing.Shortcut
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getIndexes` response payload.
 */
internal object GetIndexesParser {

    /**
     * Parses the "subsonic-response" object into an [IndexesResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [IndexesResponse].
     */
    fun parse(json: JSONObject): IndexesResponse {
        val indexesObj = json.optJSONObject("indexes")
        val lastModified = indexesObj?.optLong("lastModified", 0L) ?: 0L
        val ignoredArticles = indexesObj?.optString("ignoredArticles", "") ?: ""
        val shortcuts = indexesObj?.parseList("shortcut") { parseShortcut(it) } ?: emptyList()
        val indexes = indexesObj?.parseList("index") { parseIndexObject(it) } ?: emptyList()
        val children = indexesObj?.parseList("child") { GetSongParser.parseSong(it) } ?: emptyList()

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

    private fun parseShortcut(json: JSONObject): Shortcut = Shortcut(
        id = json.optString("id"),
        name = json.optString("name")
    )

    private fun parseIndexObject(json: JSONObject): Index? {
        val name = json.optString("name").ifEmpty { return null }
        val artists = json.parseList("artist") { parseArtist(it) }
        return Index(name = name, artists = artists)
    }

    private fun parseArtist(json: JSONObject) = ArtistEntry(
        id = json.optString("id"),
        name = json.optString("name"),
        coverArt = json.optString("coverArt").ifEmpty { null },
        artistImageUrl = json.optString("artistImageUrl").ifEmpty { null },
        starred = json.optString("starred").ifEmpty { null }
    )
}
