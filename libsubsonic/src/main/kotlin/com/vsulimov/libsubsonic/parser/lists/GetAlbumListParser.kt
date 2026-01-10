package com.vsulimov.libsubsonic.parser.lists

import com.vsulimov.libsubsonic.data.response.lists.AlbumListResponse
import com.vsulimov.libsubsonic.parser.browsing.GetAlbumParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getAlbumList2` response payload.
 */
internal object GetAlbumListParser {

    /**
     * Parses the "subsonic-response" object into an [AlbumListResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [AlbumListResponse].
     */
    fun parse(json: JSONObject): AlbumListResponse {
        val albums = json.optJSONObject("albumList2")
            ?.parseList("album") { GetAlbumParser.parseAlbum(it) }
            ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return AlbumListResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            albums = albums
        )
    }
}
