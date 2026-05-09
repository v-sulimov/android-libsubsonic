package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.CreateUserResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `createUser` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object CreateUserParser {

    /**
     * Parses the `subsonic-response` object into a [CreateUserResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [CreateUserResponse].
     */
    fun parse(json: JSONObject): CreateUserResponse = json.envelopeOnly(::CreateUserResponse)
}
