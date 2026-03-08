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
     * Parses the "subsonic-response" object into a [PlaylistResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [PlaylistResponse].
     */
    fun parse(json: JSONObject): PlaylistResponse {
        val playlistObj = json.optJSONObject("playlist")
        val playlist = if (playlistObj != null) {
            val entries = playlistObj.parseList("entry") { GetSongParser.parseSong(it) }
            GetPlaylistsParser.parsePlaylist(playlistObj).copy(entries = entries)
        } else {
            Playlist(id = "", name = "")
        }

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
