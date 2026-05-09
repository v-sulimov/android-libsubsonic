package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.DeleteUserResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `deleteUser` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object DeleteUserParser {

    /**
     * Parses the `subsonic-response` object into a [DeleteUserResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [DeleteUserResponse].
     */
    fun parse(json: JSONObject): DeleteUserResponse = json.envelopeOnly(::DeleteUserResponse)
}
