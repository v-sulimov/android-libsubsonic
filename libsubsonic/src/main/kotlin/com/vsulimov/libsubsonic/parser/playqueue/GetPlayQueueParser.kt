package com.vsulimov.libsubsonic.parser.playqueue

import com.vsulimov.libsubsonic.data.response.playqueue.PlayQueue
import com.vsulimov.libsubsonic.data.response.playqueue.PlayQueueResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.optLongOrNull
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getPlayQueue` response payload.
 */
internal object GetPlayQueueParser {

    /**
     * Parses the `subsonic-response` object into a [PlayQueueResponse].
     *
     * If the user has no saved play queue the `playQueue` element is absent and the response
     * carries `playQueue = null`.
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [PlayQueueResponse].
     */
    fun parse(json: JSONObject): PlayQueueResponse {
        val playQueue = json.optJSONObject("playQueue")?.let { obj ->
            PlayQueue(
                current = obj.optStringOrNull("current"),
                position = obj.optLongOrNull("position"),
                username = obj.optString("username"),
                changed = obj.optString("changed"),
                changedBy = obj.optString("changedBy"),
                entries = obj.parseList("entry", GetSongParser::parseSong)
            )
        }

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return PlayQueueResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            playQueue = playQueue
        )
    }
}
