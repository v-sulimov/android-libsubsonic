package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.response.playlists.PlaylistResponse
import org.json.JSONObject

/**
 * Parses the `createPlaylist` response payload.
 *
 * Since API version 1.14.0 the server returns the newly created or updated playlist
 * using the same structure as `getPlaylist`. This parser delegates to [GetPlaylistParser]
 * which already handles that structure.
 */
internal object CreatePlaylistParser {

    /**
     * Parses the "subsonic-response" object into a [PlaylistResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [PlaylistResponse].
     */
    fun parse(json: JSONObject): PlaylistResponse = GetPlaylistParser.parse(json)
}
