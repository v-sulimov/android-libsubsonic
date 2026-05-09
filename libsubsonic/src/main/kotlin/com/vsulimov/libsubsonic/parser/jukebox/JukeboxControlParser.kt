package com.vsulimov.libsubsonic.parser.jukebox

import com.vsulimov.libsubsonic.data.response.jukebox.JukeboxResponse
import com.vsulimov.libsubsonic.data.response.jukebox.JukeboxStatus
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.optIntOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `jukeboxControl` response payload.
 *
 * The server returns either a `jukeboxPlaylist` element (for the `get` action) or a
 * `jukeboxStatus` element (for every other action). Both carry the same status attributes;
 * `jukeboxPlaylist` additionally lists the playlist entries.
 */
internal object JukeboxControlParser {

    /**
     * Parses the `subsonic-response` object into a [JukeboxResponse].
     *
     * If neither element is present the response carries an empty [JukeboxStatus].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
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
            position = statusObj.optIntOrNull("position"),
            entries = statusObj.parseList("entry", GetSongParser::parseSong)
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
