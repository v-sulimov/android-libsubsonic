package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.ArtistEntry
import com.vsulimov.libsubsonic.data.response.browsing.Index
import com.vsulimov.libsubsonic.data.response.browsing.IndexesResponse
import org.json.JSONObject

/**
 * Internal parser responsible for extracting [IndexesResponse] from the JSON response.
 */
internal object GetIndexesParser {

    /**
     * Parses the "subsonic-response" object into an [IndexesResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return An [IndexesResponse] containing metadata and the list of alphabetical indexes.
     */
    fun parse(json: JSONObject): IndexesResponse {
        val indexesObj = json.optJSONObject("indexes")
        val result = mutableListOf<Index>()
        var lastModified = 0L
        var ignoredArticles = ""

        if (indexesObj != null) {
            lastModified = indexesObj.optLong("lastModified", 0L)
            ignoredArticles = indexesObj.optString("ignoredArticles", "")

            val indexArray = indexesObj.optJSONArray("index")
            if (indexArray != null) {
                for (i in 0 until indexArray.length()) {
                    parseIndexObject(json = indexArray.getJSONObject(i))?.let { result.add(it) }
                }
            } else {
                indexesObj.optJSONObject("index")?.let { index ->
                    parseIndexObject(json = index)?.let { result.add(it) }
                }
            }
        }

        return IndexesResponse(
            status = json.optString("status", "ok"),
            apiVersion = json.optString("version", "Unknown"),
            serverType = json.optString("type").ifEmpty { null },
            serverVersion = json.optString("serverVersion").ifEmpty { null },
            isOpenSubsonic = json.optBoolean("openSubsonic", false),
            lastModified = lastModified,
            ignoredArticles = ignoredArticles,
            indexes = result
        )
    }

    private fun parseIndexObject(json: JSONObject): Index? {
        val name = json.optString("name").ifEmpty { return null }
        val artists = mutableListOf<ArtistEntry>()

        val artistArray = json.optJSONArray("artist")
        if (artistArray != null) {
            for (i in 0 until artistArray.length()) {
                artists.add(parseArtist(artistArray.getJSONObject(i)))
            }
        } else {
            json.optJSONObject("artist")?.let {
                artists.add(parseArtist(it))
            }
        }

        return Index(name = name, artists = artists)
    }

    private fun parseArtist(json: JSONObject) = ArtistEntry(
        id = json.optString("id"),
        name = json.optString("name"),
        coverArt = json.optString("coverArt").ifEmpty { null },
        artistImageUrl = json.optString("artistImageUrl").ifEmpty { null }
    )
}
