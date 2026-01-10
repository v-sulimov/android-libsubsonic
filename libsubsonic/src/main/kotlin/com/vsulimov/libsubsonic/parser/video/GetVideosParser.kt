package com.vsulimov.libsubsonic.parser.video

import com.vsulimov.libsubsonic.data.response.video.VideosResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getVideos` response payload.
 */
internal object GetVideosParser {

    /**
     * Parses the "subsonic-response" object into a [VideosResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [VideosResponse].
     */
    fun parse(json: JSONObject): VideosResponse {
        val videos = json.optJSONObject("videos")
            ?.parseList("video") { GetSongParser.parseSong(it) }
            ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return VideosResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            videos = videos
        )
    }
}
