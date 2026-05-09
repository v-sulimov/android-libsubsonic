package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.response.playlists.Playlist
import com.vsulimov.libsubsonic.data.response.playlists.PlaylistResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getPlaylist` response payload.
 */
internal object GetPlaylistParser {

    /**
     * Parses the `subsonic-response` object into a [PlaylistResponse].
     *
     * If the `playlist` element is missing the response carries an empty placeholder [Playlist].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [PlaylistResponse].
     */
    fun parse(json: JSONObject): PlaylistResponse {
        val playlist = json.optJSONObject("playlist")?.let { obj ->
            val entries = obj.parseList("entry", GetSongParser::parseSong)
            GetPlaylistsParser.parsePlaylist(obj).copy(entries = entries)
        } ?: Playlist(id = "", name = "")

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return PlaylistResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            playlist = playlist
        )
    }
}
