package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.response.playlists.Playlist
import com.vsulimov.libsubsonic.data.response.playlists.PlaylistsResponse
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONArray
import org.json.JSONObject

/**
 * Parses the `getPlaylists` response payload and provides the shared [parsePlaylist] helper
 * used by [GetPlaylistParser] for the playlist header fields.
 */
internal object GetPlaylistsParser {

    /**
     * Parses the `subsonic-response` object into a [PlaylistsResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [PlaylistsResponse].
     */
    fun parse(json: JSONObject): PlaylistsResponse {
        val playlists = json.optJSONObject("playlists")
            ?.parseList("playlist", ::parsePlaylist)
            .orEmpty()

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
     * Parses a single `playlist` JSON object into a [Playlist] with an empty `entries` list.
     *
     * Shared with [GetPlaylistParser]: callers that have the playlist's full body can append
     * entries via [Playlist.copy] when needed.
     *
     * The `allowedUser` field can be either a JSON array of usernames or a single string when
     * exactly one user is permitted; both shapes are accepted.
     *
     * @param json The JSON object representing a playlist.
     * @return The parsed [Playlist] with no embedded entries.
     */
    internal fun parsePlaylist(json: JSONObject): Playlist {
        val allowedUsers = when (val value = json.opt("allowedUser")) {
            is JSONArray -> (0 until value.length()).map(value::getString)
            is String -> listOf(value)
            else -> emptyList()
        }
        return Playlist(
            id = json.optString("id"),
            name = json.optString("name"),
            comment = json.optStringOrNull("comment"),
            owner = json.optStringOrNull("owner"),
            public = json.optBoolean("public", false),
            songCount = json.optInt("songCount", 0),
            duration = json.optInt("duration", 0),
            created = json.optStringOrNull("created"),
            coverArt = json.optStringOrNull("coverArt"),
            allowedUsers = allowedUsers
        )
    }
}
