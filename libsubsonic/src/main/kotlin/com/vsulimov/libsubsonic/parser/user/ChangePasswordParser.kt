package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.ChangePasswordResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `changePassword` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object ChangePasswordParser {

    /**
     * Parses the "subsonic-response" object into a [ChangePasswordResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [ChangePasswordResponse].
     */
    fun parse(json: JSONObject): ChangePasswordResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return ChangePasswordResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
