package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.MusicFolder
import com.vsulimov.libsubsonic.data.response.browsing.MusicFoldersResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getMusicFolders` response payload.
 */
internal object GetMusicFoldersParser {

    /**
     * Extracts music folders and metadata from the "subsonic-response" object.
     *
     * @param json The "subsonic-response" JSONObject.
     * @return The parsed [MusicFoldersResponse].
     */
    fun parse(json: JSONObject): MusicFoldersResponse {
        val folders = json.optJSONObject("musicFolders")
            ?.parseList("musicFolder") { MusicFolder(id = it.optString("id"), name = it.optString("name")) }
            ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return MusicFoldersResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            musicFolders = folders
        )
    }
}
