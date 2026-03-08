package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.response.user.User
import com.vsulimov.libsubsonic.data.response.user.UserResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `getUser` response payload.
 */
internal object GetUserParser {

    /**
     * Parses the "subsonic-response" object into a [UserResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [UserResponse].
     */
    fun parse(json: JSONObject): UserResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return UserResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            user = parseUser(json.getJSONObject("user"))
        )
    }

    /**
     * Parses a single user JSON object into a [User].
     *
     * This function is shared with [GetUsersParser] which uses it to parse
     * individual user entries from the `users` list.
     *
     * @param userJson The JSON object representing a single user.
     * @return The parsed [User].
     */
    internal fun parseUser(userJson: JSONObject): User {
        val folderArray = userJson.optJSONArray("folder")
        val folders = if (folderArray != null) {
            (0 until folderArray.length()).map { folderArray.getInt(it) }
        } else {
            emptyList()
        }
        return User(
            username = userJson.optString("username"),
            email = userJson.optString("email").ifEmpty { null },
            scrobblingEnabled = userJson.optBoolean("scrobblingEnabled"),
            adminRole = userJson.optBoolean("adminRole"),
            settingsRole = userJson.optBoolean("settingsRole"),
            downloadRole = userJson.optBoolean("downloadRole"),
            uploadRole = userJson.optBoolean("uploadRole"),
            playlistRole = userJson.optBoolean("playlistRole"),
            coverArtRole = userJson.optBoolean("coverArtRole"),
            commentRole = userJson.optBoolean("commentRole"),
            podcastRole = userJson.optBoolean("podcastRole"),
            streamRole = userJson.optBoolean("streamRole"),
            jukeboxRole = userJson.optBoolean("jukeboxRole"),
            shareRole = userJson.optBoolean("shareRole"),
            folders = folders
        )
    }
}
