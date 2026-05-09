package com.vsulimov.libsubsonic.parser.annotation

import com.vsulimov.libsubsonic.data.response.annotation.ScrobbleResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `scrobble` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object ScrobbleParser {

    /**
     * Parses the `subsonic-response` object into a [ScrobbleResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [ScrobbleResponse].
     */
    fun parse(json: JSONObject): ScrobbleResponse = json.envelopeOnly(::ScrobbleResponse)
}
