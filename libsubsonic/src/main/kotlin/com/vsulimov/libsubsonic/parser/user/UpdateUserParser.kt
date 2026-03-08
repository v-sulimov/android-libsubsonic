package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.UpdateUserResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `updateUser` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object UpdateUserParser {

    /**
     * Parses the "subsonic-response" object into an [UpdateUserResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [UpdateUserResponse].
     */
    fun parse(json: JSONObject): UpdateUserResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return UpdateUserResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
