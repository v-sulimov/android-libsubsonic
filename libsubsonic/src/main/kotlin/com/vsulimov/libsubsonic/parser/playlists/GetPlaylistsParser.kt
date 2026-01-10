package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.response.playlists.Playlist
import com.vsulimov.libsubsonic.data.response.playlists.PlaylistsResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONArray
import org.json.JSONObject

/**
 * Parses the `getPlaylists` response payload.
 */
internal object GetPlaylistsParser {

    /**
     * Parses the "subsonic-response" object into a [PlaylistsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [PlaylistsResponse].
     */
    fun parse(json: JSONObject): PlaylistsResponse {
        val playlists = json.optJSONObject("playlists")
            ?.parseList("playlist") { parsePlaylist(it) }
            ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return PlaylistsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            playlists = playlists
        )
    }

    /**
     * Parses a single playlist JSON object into a [Playlist] without embedded entries.
     *
     * This function is shared with [GetPlaylistParser] which uses it to parse the playlist
     * header fields. Entries can be appended via [Playlist.copy] when needed.
     *
     * @param json The JSON object representing a playlist.
     * @return The parsed [Playlist] with an empty entries list.
     */
    internal fun parsePlaylist(json: JSONObject): Playlist {
        val allowedUsers = when (val value = json.opt("allowedUser")) {
            is JSONArray -> (0 until value.length()).map { i -> value.getString(i) }
            is String -> listOf(value)
            else -> emptyList()
        }
        return Playlist(
            id = json.optString("id"),
            name = json.optString("name"),
            comment = json.optString("comment").ifEmpty { null },
            owner = json.optString("owner").ifEmpty { null },
            public = json.optBoolean("public", false),
            songCount = json.optInt("songCount", 0),
            duration = json.optInt("duration", 0),
            created = json.optString("created").ifEmpty { null },
            coverArt = json.optString("coverArt").ifEmpty { null },
            allowedUsers = allowedUsers
        )
    }
}
