package com.vsulimov.libsubsonic.parser.lyrics

import com.vsulimov.libsubsonic.data.response.lyrics.Lyrics
import com.vsulimov.libsubsonic.data.response.lyrics.LyricsResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `getLyrics` response payload.
 */
internal object GetLyricsParser {

    /**
     * Parses the "subsonic-response" object into a [LyricsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [LyricsResponse].
     */
    fun parse(json: JSONObject): LyricsResponse {
        val lyricsObj = json.optJSONObject("lyrics")
        val lyrics = if (lyricsObj != null) {
            Lyrics(
                artist = lyricsObj.optString("artist").ifEmpty { null },
                title = lyricsObj.optString("title").ifEmpty { null },
                value = lyricsObj.optString("value").ifEmpty { null }
            )
        } else {
            Lyrics()
        }

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return LyricsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            lyrics = lyrics
        )
    }
}
