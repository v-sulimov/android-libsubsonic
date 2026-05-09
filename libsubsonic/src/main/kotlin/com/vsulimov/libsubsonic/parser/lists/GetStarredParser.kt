package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.response.lists.StarredResponse
import com.vsulimov.libsubsonic.parser.browsing.GetAlbumParser
import com.vsulimov.libsubsonic.parser.browsing.GetArtistsParser
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getStarred2` response payload.
 */
internal object GetStarredParser {

    /**
     * Parses the `subsonic-response` object into a [StarredResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [StarredResponse].
     */
    fun parse(json: JSONObject): StarredResponse {
        val container = json.optJSONObject("starred2")
        val artists = container?.parseList("artist", GetArtistsParser::parseSingleArtist).orEmpty()
        val albums = container?.parseList("album", GetAlbumParser::parseAlbum).orEmpty()
        val songs = container?.parseList("song", GetSongParser::parseSong).orEmpty()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return StarredResponse(
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
