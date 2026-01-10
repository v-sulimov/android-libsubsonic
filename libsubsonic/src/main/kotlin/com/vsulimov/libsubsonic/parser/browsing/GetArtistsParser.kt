package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Artist
import com.vsulimov.libsubsonic.data.response.browsing.ArtistIndex
import com.vsulimov.libsubsonic.data.response.browsing.ArtistsResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getArtists` response payload.
 */
internal object GetArtistsParser {

    /**
     * Parses the "subsonic-response" object into an [ArtistsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [ArtistsResponse].
     */
    fun parse(json: JSONObject): ArtistsResponse {
        val artistsObj = json.optJSONObject("artists")
        val ignoredArticles = artistsObj?.optString("ignoredArticles", "") ?: ""
        val artists = artistsObj?.parseList("index") { parseArtistIndex(it) } ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return ArtistsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            ignoredArticles = ignoredArticles,
            artists = artists
        )
    }

    private fun parseArtistIndex(json: JSONObject): ArtistIndex? {
        val name = json.optString("name").ifEmpty { return null }
        val artists = json.parseList("artist") { parseSingleArtist(it) }
        return ArtistIndex(name = name, artists = artists)
    }

    /**
     * Parses a single artist JSON object into an [Artist].
     *
     * This function is shared with [GetArtistInfoParser] which uses it to parse
     * similar artist entries.
     *
     * @param json The JSON object representing an artist.
     * @return The parsed [Artist].
     */
    internal fun parseSingleArtist(json: JSONObject): Artist {
        val roles = json.optJSONArray("roles")?.let { array ->
            (0 until array.length()).map { i -> array.getString(i) }
        } ?: emptyList()

        return Artist(
            id = json.optString("id"),
            name = json.optString("name"),
            albumCount = json.optInt("albumCount", 0),
            starred = json.optString("starred").ifEmpty { null },
            coverArt = json.optString("coverArt").ifEmpty { null },
            artistImageUrl = json.optString("artistImageUrl").ifEmpty { null },
            musicBrainzId = json.optString("musicBrainzId").ifEmpty { null },
            sortName = json.optString("sortName").ifEmpty { null },
            roles = roles
        )
    }
}
