package com.vsulimov.libsubsonic.parser.playqueue

import com.vsulimov.libsubsonic.data.response.playqueue.SavePlayQueueResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `savePlayQueue` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object SavePlayQueueParser {

    /**
     * Parses the `subsonic-response` object into a [SavePlayQueueResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [SavePlayQueueResponse].
     */
    fun parse(json: JSONObject): SavePlayQueueResponse = json.envelopeOnly(::SavePlayQueueResponse)
}
