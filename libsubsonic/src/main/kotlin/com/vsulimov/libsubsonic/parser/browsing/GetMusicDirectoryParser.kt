package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Directory
import com.vsulimov.libsubsonic.data.response.browsing.MusicDirectoryResponse
import com.vsulimov.libsubsonic.parser.optIntOrNull
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getMusicDirectory` response payload.
 */
internal object GetMusicDirectoryParser {

    /**
     * Parses the `subsonic-response` object into a [MusicDirectoryResponse].
     *
     * If the `directory` element is missing the response carries an empty placeholder [Directory].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [MusicDirectoryResponse].
     */
    fun parse(json: JSONObject): MusicDirectoryResponse {
        val directory = json.optJSONObject("directory")?.let { obj ->
            Directory(
                id = obj.optString("id"),
                parent = obj.optStringOrNull("parent"),
                name = obj.optString("name"),
                starred = obj.optStringOrNull("starred"),
                playCount = obj.optIntOrNull("playCount"),
                played = obj.optStringOrNull("played"),
                albumCount = obj.optIntOrNull("albumCount"),
                children = obj.parseList("child", GetSongParser::parseSong)
            )
        } ?: Directory(id = "", name = "", children = emptyList())

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
