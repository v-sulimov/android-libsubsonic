package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Artist
import com.vsulimov.libsubsonic.data.response.browsing.ArtistIndex
import com.vsulimov.libsubsonic.data.response.browsing.ArtistsResponse
import com.vsulimov.libsubsonic.parser.optStringList
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getArtists` response payload and provides the shared [parseSingleArtist] helper
 * used by [GetArtistInfoParser] for similar-artist entries.
 */
internal object GetArtistsParser {

    /**
     * Parses the `subsonic-response` object into an [ArtistsResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [ArtistsResponse].
     */
    fun parse(json: JSONObject): ArtistsResponse {
        val artistsObj = json.optJSONObject("artists")
        val ignoredArticles = artistsObj?.optString("ignoredArticles", "") ?: ""
        val artists = artistsObj?.parseList("index", ::parseArtistIndex) ?: emptyList()

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

    /**
     * Parses an alphabetical index group into an [ArtistIndex].
     *
     * @param json The JSON object representing an index entry containing the index letter and
     *   its artists.
     * @return The parsed [ArtistIndex], or `null` if the index has no name (which would
     *   indicate a malformed payload).
     */
    private fun parseArtistIndex(json: JSONObject): ArtistIndex? {
        val name = json.optString("name").ifEmpty { return null }
        return ArtistIndex(name = name, artists = json.parseList("artist", ::parseSingleArtist))
    }

    /**
     * Parses a single `artist` JSON object into an [Artist].
     *
     * Shared with [GetArtistInfoParser] for parsing similar-artist entries.
     *
     * @param json The JSON object representing an artist.
     * @return The parsed [Artist].
     */
    internal fun parseSingleArtist(json: JSONObject) = Artist(
        id = json.optString("id"),
        name = json.optString("name"),
        albumCount = json.optInt("albumCount", 0),
        starred = json.optStringOrNull("starred"),
        coverArt = json.optStringOrNull("coverArt"),
        artistImageUrl = json.optStringOrNull("artistImageUrl"),
        musicBrainzId = json.optStringOrNull("musicBrainzId"),
        sortName = json.optStringOrNull("sortName"),
        roles = json.optStringList("roles")
    )
}
