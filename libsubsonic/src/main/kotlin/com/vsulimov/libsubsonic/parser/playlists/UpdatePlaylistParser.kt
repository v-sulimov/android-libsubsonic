package com.vsulimov.libsubsonic.parser.playlists

import com.vsulimov.libsubsonic.data.response.playlists.UpdatePlaylistResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `updatePlaylist` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object UpdatePlaylistParser {

    /**
     * Parses the `subsonic-response` object into an [UpdatePlaylistResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [UpdatePlaylistResponse].
     */
    fun parse(json: JSONObject): UpdatePlaylistResponse = json.envelopeOnly(::UpdatePlaylistResponse)
}
