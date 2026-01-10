package com.vsulimov.libsubsonic.parser.search

import com.vsulimov.libsubsonic.data.response.search.SearchResponse
import com.vsulimov.libsubsonic.parser.browsing.GetAlbumParser
import com.vsulimov.libsubsonic.parser.browsing.GetArtistsParser
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `search` response payload.
 */
internal object SearchParser {

    /**
     * Parses the "subsonic-response" object into a [SearchResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [SearchResponse].
     */
    fun parse(json: JSONObject): SearchResponse {
        val container = json.optJSONObject("searchResult3")
        val artists = container?.parseList("artist") { GetArtistsParser.parseSingleArtist(it) } ?: emptyList()
        val albums = container?.parseList("album") { GetAlbumParser.parseAlbum(it) } ?: emptyList()
        val songs = container?.parseList("song") { GetSongParser.parseSong(it) } ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return SearchResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            artists = artists,
            albums = albums,
            songs = songs
        )
    }
}
