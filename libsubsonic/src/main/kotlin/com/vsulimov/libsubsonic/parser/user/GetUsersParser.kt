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
     * Parses the "subsonic-response" object into a [UsersResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [UsersResponse].
     */
    fun parse(json: JSONObject): UsersResponse {
        val containerObj = json.optJSONObject("users")
        val users = containerObj?.parseList("user") { userJson ->
            GetUserParser.parseUser(userJson)
        } ?: emptyList()

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
