package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.CreateUserResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `createUser` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object CreateUserParser {

    /**
     * Parses the "subsonic-response" object into a [CreateUserResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [CreateUserResponse].
     */
    fun parse(json: JSONObject): CreateUserResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return CreateUserResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
