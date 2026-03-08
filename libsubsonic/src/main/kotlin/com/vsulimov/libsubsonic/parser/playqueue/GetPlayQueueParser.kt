package com.vsulimov.libsubsonic.parser.playqueue

import com.vsulimov.libsubsonic.data.response.playqueue.PlayQueue
import com.vsulimov.libsubsonic.data.response.playqueue.PlayQueueResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getPlayQueue` response payload.
 */
internal object GetPlayQueueParser {

    /**
     * Parses the "subsonic-response" object into a [PlayQueueResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [PlayQueueResponse].
     */
    fun parse(json: JSONObject): PlayQueueResponse {
        val queueJson = json.optJSONObject("playQueue")
        val playQueue = queueJson?.let {
            val entries = it.parseList("entry") { entryJson ->
                GetSongParser.parseSong(entryJson)
            }
            PlayQueue(
                current = it.optString("current").ifEmpty { null },
                position = if (it.has("position")) it.optLong("position") else null,
                username = it.optString("username"),
                changed = it.optString("changed"),
                changedBy = it.optString("changedBy"),
                entries = entries
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
