package com.vsulimov.libsubsonic.parser.system

import com.vsulimov.libsubsonic.data.response.system.PingResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `ping` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object PingParser {

    /**
     * Parses the `subsonic-response` object into a [PingResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [PingResponse].
     */
    fun parse(json: JSONObject): PingResponse = json.envelopeOnly(::PingResponse)
}
