package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.ChangePasswordResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `changePassword` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object ChangePasswordParser {

    /**
     * Parses the `subsonic-response` object into a [ChangePasswordResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [ChangePasswordResponse].
     */
    fun parse(json: JSONObject): ChangePasswordResponse = json.envelopeOnly(::ChangePasswordResponse)
}
