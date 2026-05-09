package com.vsulimov.libsubsonic.parser.sharing

import com.vsulimov.libsubsonic.data.response.sharing.Share
import com.vsulimov.libsubsonic.data.response.sharing.SharesResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getShares` response payload (also reused by `createShare`).
 */
internal object GetSharesParser {

    /**
     * Parses the `subsonic-response` object into a [SharesResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [SharesResponse].
     */
    fun parse(json: JSONObject): SharesResponse {
        val shares = json.optJSONObject("shares")
            ?.parseList("share", ::parseShare)
            .orEmpty()

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

    private fun parseShare(json: JSONObject) = Share(
        id = json.optString("id"),
        url = json.optString("url"),
        description = json.optStringOrNull("description"),
        username = json.optString("username"),
        created = json.optString("created"),
        lastVisited = json.optStringOrNull("lastVisited"),
        expires = json.optStringOrNull("expires"),
        visitCount = json.optInt("visitCount", 0),
        entries = json.parseList("entry", GetSongParser::parseSong)
    )
}
