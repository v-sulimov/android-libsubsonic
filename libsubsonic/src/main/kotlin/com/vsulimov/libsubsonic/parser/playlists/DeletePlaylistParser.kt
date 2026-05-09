package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.response.playlists.DeletePlaylistResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `deletePlaylist` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object DeletePlaylistParser {

    /**
     * Parses the `subsonic-response` object into a [DeletePlaylistResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [DeletePlaylistResponse].
     */
    fun parse(json: JSONObject): DeletePlaylistResponse = json.envelopeOnly(::DeletePlaylistResponse)
}
