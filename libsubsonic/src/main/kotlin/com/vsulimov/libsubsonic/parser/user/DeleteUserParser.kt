package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.DeleteUserResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `deleteUser` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object DeleteUserParser {

    /**
     * Parses the "subsonic-response" object into a [DeleteUserResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [DeleteUserResponse].
     */
    fun parse(json: JSONObject): DeleteUserResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return DeleteUserResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
