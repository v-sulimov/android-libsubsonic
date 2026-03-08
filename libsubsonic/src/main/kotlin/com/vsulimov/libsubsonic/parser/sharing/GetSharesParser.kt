package com.vsulimov.libsubsonic.parser.sharing

import com.vsulimov.libsubsonic.data.response.sharing.Share
import com.vsulimov.libsubsonic.data.response.sharing.SharesResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getShares` response payload.
 */
internal object GetSharesParser {

    /**
     * Parses the "subsonic-response" object into a [SharesResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [SharesResponse].
     */
    fun parse(json: JSONObject): SharesResponse {
        val sharesObj = json.optJSONObject("shares")
        val shares = sharesObj?.parseList("share") { shareJson ->
            Share(
                id = shareJson.optString("id"),
                url = shareJson.optString("url"),
                description = shareJson.optString("description").ifEmpty { null },
                username = shareJson.optString("username"),
                created = shareJson.optString("created"),
                lastVisited = shareJson.optString("lastVisited").ifEmpty { null },
                expires = shareJson.optString("expires").ifEmpty { null },
                visitCount = shareJson.optInt("visitCount", 0),
                entries = shareJson.parseList("entry") { GetSongParser.parseSong(it) }
            )
        } ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return SharesResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            shares = shares
        )
    }
}
