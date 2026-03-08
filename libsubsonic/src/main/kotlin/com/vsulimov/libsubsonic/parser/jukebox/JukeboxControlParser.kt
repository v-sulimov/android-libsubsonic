package com.vsulimov.libsubsonic.parser.jukebox

import com.vsulimov.libsubsonic.data.response.jukebox.JukeboxResponse
import com.vsulimov.libsubsonic.data.response.jukebox.JukeboxStatus
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `jukeboxControl` response payload.
 *
 * The server returns either a `jukeboxPlaylist` element (for the `get` action) or a
 * `jukeboxStatus` element (for all other actions). Both share the same attributes;
 * `jukeboxPlaylist` additionally contains `entry` children.
 */
internal object JukeboxControlParser {

    /**
     * Parses the "subsonic-response" object into a [JukeboxResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [JukeboxResponse].
     */
    fun parse(json: JSONObject): JukeboxResponse {
        val statusObj = json.optJSONObject("jukeboxPlaylist")
            ?: json.optJSONObject("jukeboxStatus")
            ?: JSONObject()

        val jukeboxStatus = JukeboxStatus(
            currentIndex = statusObj.optInt("currentIndex", 0),
            playing = statusObj.optBoolean("playing", false),
            gain = statusObj.optDouble("gain", 0.0),
            position = if (statusObj.has("position")) statusObj.optInt("position") else null,
            entries = statusObj.parseList("entry") { GetSongParser.parseSong(it) }
        )

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return JukeboxResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            jukeboxStatus = jukeboxStatus
        )
    }
}
