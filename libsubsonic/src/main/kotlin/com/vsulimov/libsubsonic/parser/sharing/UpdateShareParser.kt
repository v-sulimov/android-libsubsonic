package com.vsulimov.libsubsonic.parser.sharing

import com.vsulimov.libsubsonic.data.response.sharing.UpdateShareResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `updateShare` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object UpdateShareParser {

    /**
     * Parses the `subsonic-response` object into an [UpdateShareResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [UpdateShareResponse].
     */
    fun parse(json: JSONObject): UpdateShareResponse = json.envelopeOnly(::UpdateShareResponse)
}
