package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Directory
import com.vsulimov.libsubsonic.data.response.browsing.MusicDirectoryResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getMusicDirectory` response payload.
 */
internal object GetMusicDirectoryParser {

    /**
     * Parses the "subsonic-response" object into a [MusicDirectoryResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [MusicDirectoryResponse].
     */
    fun parse(json: JSONObject): MusicDirectoryResponse {
        val directoryObj = json.optJSONObject("directory")
        val directory = if (directoryObj != null) {
            Directory(
                id = directoryObj.optString("id"),
                parent = directoryObj.optString("parent").ifEmpty { null },
                name = directoryObj.optString("name"),
                starred = directoryObj.optString("starred").ifEmpty { null },
                playCount = if (directoryObj.has("playCount")) directoryObj.optInt("playCount") else null,
                played = directoryObj.optString("played").ifEmpty { null },
                albumCount = if (directoryObj.has("albumCount")) directoryObj.optInt("albumCount") else null,
                children = directoryObj.parseList("child") { GetSongParser.parseSong(it) }
            )
        } else {
            Directory(id = "", name = "", children = emptyList())
        }

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return MusicDirectoryResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            directory = directory
        )
    }
}
