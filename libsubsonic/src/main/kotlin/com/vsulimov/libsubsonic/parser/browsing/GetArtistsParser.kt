package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Artist
import com.vsulimov.libsubsonic.data.response.browsing.ArtistIndex
import com.vsulimov.libsubsonic.data.response.browsing.ArtistsResponse
import org.json.JSONObject

/**
 * Internal parser for the `getArtists` ID3-based response.
 */
internal object GetArtistsParser {

    /**
     * Parses the "subsonic-response" object into an [ArtistsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     */
    fun parse(json: JSONObject): ArtistsResponse {
        val artistsObj = json.optJSONObject("artists")
        val result = mutableListOf<ArtistIndex>()
        var lastModified = 0L
        var ignoredArticles = ""

        if (artistsObj != null) {
            lastModified = artistsObj.optLong("lastModified", 0L)
            ignoredArticles = artistsObj.optString("ignoredArticles", "")

            val indexArray = artistsObj.optJSONArray("index")
            if (indexArray != null) {
                for (i in 0 until indexArray.length()) {
                    parseArtistIndex(json = indexArray.getJSONObject(i))?.let { result.add(it) }
                }
            } else {
                artistsObj.optJSONObject("index")?.let { artistIndex ->
                    parseArtistIndex(json = artistIndex)?.let { result.add(it) }
                }
            }
        }

        return ArtistsResponse(
            status = json.optString("status", "ok"),
            apiVersion = json.optString("version", "Unknown"),
            serverType = json.optString("type").ifEmpty { null },
            serverVersion = json.optString("serverVersion").ifEmpty { null },
            isOpenSubsonic = json.optBoolean("openSubsonic", false),
            lastModified = lastModified,
            ignoredArticles = ignoredArticles,
            artists = result
        )
    }

    private fun parseArtistIndex(json: JSONObject): ArtistIndex? {
        val name = json.optString("name").ifEmpty { return null }
        val artists = mutableListOf<Artist>()

        val artistArray = json.optJSONArray("artist")
        if (artistArray != null) {
            for (i in 0 until artistArray.length()) {
                artists.add(parseSingleArtist(artistArray.getJSONObject(i)))
            }
        } else {
            json.optJSONObject("artist")?.let {
                artists.add(parseSingleArtist(it))
            }
        }

        return ArtistIndex(name = name, artists = artists)
    }

    private fun parseSingleArtist(json: JSONObject): Artist {
        val roles = mutableListOf<String>()
        val rolesArray = json.optJSONArray("roles")
        if (rolesArray != null) {
            for (i in 0 until rolesArray.length()) {
                roles.add(rolesArray.getString(i))
            }
        }

        return Artist(
            id = json.optString("id"),
            name = json.optString("name"),
            albumCount = json.optInt("albumCount", 0),
            coverArt = json.optString("coverArt").ifEmpty { null },
            artistImageUrl = json.optString("artistImageUrl").ifEmpty { null },
            musicBrainzId = json.optString("musicBrainzId").ifEmpty { null },
            sortName = json.optString("sortName").ifEmpty { null },
            roles = roles
        )
    }
}
