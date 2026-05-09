package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.UsersResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getUsers` response payload.
 */
internal object GetUsersParser {

    /**
     * Parses the `subsonic-response` object into a [UsersResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [UsersResponse].
     */
    fun parse(json: JSONObject): UsersResponse {
        val users = json.optJSONObject("users")
            ?.parseList("user", GetUserParser::parseUser)
            .orEmpty()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return UsersResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            users = users
        )
    }
}
