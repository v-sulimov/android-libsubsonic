package com.vsulimov.libsubsonic.parser.sharing

import com.vsulimov.libsubsonic.data.response.sharing.DeleteShareResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `deleteShare` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object DeleteShareParser {

    /**
     * Parses the `subsonic-response` object into a [DeleteShareResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [DeleteShareResponse].
     */
    fun parse(json: JSONObject): DeleteShareResponse = json.envelopeOnly(::DeleteShareResponse)
}
