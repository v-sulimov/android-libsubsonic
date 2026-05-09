package com.vsulimov.libsubsonic.parser.annotation

import com.vsulimov.libsubsonic.data.response.annotation.UnstarResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `unstar` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object UnstarParser {

    /**
     * Parses the `subsonic-response` object into an [UnstarResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [UnstarResponse].
     */
    fun parse(json: JSONObject): UnstarResponse = json.envelopeOnly(::UnstarResponse)
}
