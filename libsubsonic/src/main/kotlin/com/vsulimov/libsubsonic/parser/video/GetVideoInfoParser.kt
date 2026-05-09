package com.vsulimov.libsubsonic.parser.video

import com.vsulimov.libsubsonic.data.response.video.AudioTrack
import com.vsulimov.libsubsonic.data.response.video.Caption
import com.vsulimov.libsubsonic.data.response.video.Conversion
import com.vsulimov.libsubsonic.data.response.video.VideoInfo
import com.vsulimov.libsubsonic.data.response.video.VideoInfoResponse
import com.vsulimov.libsubsonic.parser.optIntOrNull
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getVideoInfo` response payload.
 */
internal object GetVideoInfoParser {

    /**
     * Parses the `subsonic-response` object into a [VideoInfoResponse].
     *
     * If the `videoInfo` element is missing the response carries an empty placeholder [VideoInfo].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [VideoInfoResponse].
     */
    fun parse(json: JSONObject): VideoInfoResponse {
        val videoInfo = json.optJSONObject("videoInfo")?.let { obj ->
            VideoInfo(
                id = obj.optString("id"),
                captions = obj.parseList("captions", ::parseCaption),
                audioTracks = obj.parseList("audioTrack", ::parseAudioTrack),
                conversions = obj.parseList("conversion", ::parseConversion)
            )
        } ?: VideoInfo(id = "")

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

    private fun parseCaption(json: JSONObject) = Caption(
        id = json.optString("id"),
        name = json.optString("name")
    )

    private fun parseAudioTrack(json: JSONObject) = AudioTrack(
        id = json.optString("id"),
        name = json.optString("name"),
        languageCode = json.optStringOrNull("languageCode")
    )

    private fun parseConversion(json: JSONObject) = Conversion(
        id = json.optString("id"),
        bitRate = json.optIntOrNull("bitRate")
    )
}
