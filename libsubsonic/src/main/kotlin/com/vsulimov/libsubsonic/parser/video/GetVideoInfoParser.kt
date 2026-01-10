package com.vsulimov.libsubsonic.parser.video

import com.vsulimov.libsubsonic.data.response.video.AudioTrack
import com.vsulimov.libsubsonic.data.response.video.Caption
import com.vsulimov.libsubsonic.data.response.video.Conversion
import com.vsulimov.libsubsonic.data.response.video.VideoInfo
import com.vsulimov.libsubsonic.data.response.video.VideoInfoResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getVideoInfo` response payload.
 */
internal object GetVideoInfoParser {

    /**
     * Parses the "subsonic-response" object into a [VideoInfoResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [VideoInfoResponse].
     */
    fun parse(json: JSONObject): VideoInfoResponse {
        val infoObj = json.optJSONObject("videoInfo")
        val videoInfo = if (infoObj != null) {
            VideoInfo(
                id = infoObj.optString("id"),
                captions = infoObj.parseList("captions") { obj ->
                    Caption(
                        id = obj.optString("id"),
                        name = obj.optString("name")
                    )
                },
                audioTracks = infoObj.parseList("audioTrack") { obj ->
                    AudioTrack(
                        id = obj.optString("id"),
                        name = obj.optString("name"),
                        languageCode = obj.optString("languageCode").ifEmpty { null }
                    )
                },
                conversions = infoObj.parseList("conversion") { obj ->
                    Conversion(
                        id = obj.optString("id"),
                        bitRate = if (obj.has("bitRate")) obj.optInt("bitRate") else null
                    )
                }
            )
        } else {
            VideoInfo(id = "")
        }

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return VideoInfoResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            videoInfo = videoInfo
        )
    }
}
