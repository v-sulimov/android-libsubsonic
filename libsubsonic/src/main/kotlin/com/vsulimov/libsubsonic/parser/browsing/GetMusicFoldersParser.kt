package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.MusicFolder
import com.vsulimov.libsubsonic.data.response.browsing.MusicFoldersResponse
import org.json.JSONObject

/**
 * Internal parser responsible for extracting [MusicFoldersResponse] from the JSON response.
 */
internal object GetMusicFoldersParser {

    /**
     * Extracts music folders and metadata from the "subsonic-response" object.
     *
     * @param json The "subsonic-response" JSONObject.
     * @return A [MusicFoldersResponse] containing metadata and the list of folders.
     */
    fun parse(json: JSONObject): MusicFoldersResponse {
        val musicFoldersObj = json.optJSONObject("musicFolders")
        val folders = mutableListOf<MusicFolder>()

        if (musicFoldersObj != null) {
            val musicFolderArray = musicFoldersObj.optJSONArray("musicFolder")
            if (musicFolderArray != null) {
                for (i in 0 until musicFolderArray.length()) {
                    folders.add(parseSingleFolder(musicFolderArray.getJSONObject(i)))
                }
            } else {
                musicFoldersObj.optJSONObject("musicFolder")?.let {
                    folders.add(parseSingleFolder(it))
                }
            }
        }

        return MusicFoldersResponse(
            status = json.optString("status", "ok"),
            apiVersion = json.optString("version", "Unknown"),
            serverType = json.optString("type").ifEmpty { null },
            serverVersion = json.optString("serverVersion").ifEmpty { null },
            isOpenSubsonic = json.optBoolean("openSubsonic", false),
            musicFolders = folders
        )
    }

    private fun parseSingleFolder(json: JSONObject): MusicFolder = MusicFolder(
        id = json.optString("id"),
        name = json.optString("name")
    )
}
