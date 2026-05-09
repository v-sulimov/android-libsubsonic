package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.UpdateUserResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `updateUser` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object UpdateUserParser {

    /**
     * Parses the `subsonic-response` object into an [UpdateUserResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [UpdateUserResponse].
     */
    fun parse(json: JSONObject): UpdateUserResponse = json.envelopeOnly(::UpdateUserResponse)
}
