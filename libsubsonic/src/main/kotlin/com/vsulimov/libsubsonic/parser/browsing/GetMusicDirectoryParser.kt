package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Child
import com.vsulimov.libsubsonic.data.response.browsing.Directory
import com.vsulimov.libsubsonic.data.response.browsing.MusicDirectoryResponse
import org.json.JSONObject

/**
 * Internal parser for the `getMusicDirectory` response.
 */
internal object GetMusicDirectoryParser {

    /**
     * Parses the "subsonic-response" object into a [MusicDirectoryResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     */
    fun parse(json: JSONObject): MusicDirectoryResponse {
        val directoryObj = json.optJSONObject("directory")
        val directory = if (directoryObj != null) {
            val children = mutableListOf<Child>()
            val childArray = directoryObj.optJSONArray("child")

            if (childArray != null) {
                for (i in 0 until childArray.length()) {
                    children.add(GetSongParser.parseSong(json = childArray.getJSONObject(i)))
                }
            } else {
                directoryObj.optJSONObject("child")?.let {
                    children.add(GetSongParser.parseSong(it))
                }
            }

            Directory(
                id = directoryObj.optString("id"),
                name = directoryObj.optString("name"),
                playCount = if (directoryObj.has("playCount")) directoryObj.optInt("playCount") else null,
                played = directoryObj.optString("played").ifEmpty { null },
                albumCount = if (directoryObj.has("albumCount")) directoryObj.optInt("albumCount") else null,
                children = children
            )
        } else {
            Directory(id = "", name = "", children = emptyList())
        }

        return MusicDirectoryResponse(
            status = json.optString("status", "ok"),
            apiVersion = json.optString("version", "Unknown"),
            serverType = json.optString("type").ifEmpty { null },
            serverVersion = json.optString("serverVersion").ifEmpty { null },
            isOpenSubsonic = json.optBoolean("openSubsonic", false),
            directory = directory
        )
    }
}
