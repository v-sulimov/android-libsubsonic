package com.vsulimov.libsubsonic.parser.lyrics

import com.vsulimov.libsubsonic.data.response.lyrics.Lyrics
import com.vsulimov.libsubsonic.data.response.lyrics.LyricsResponse
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `getLyrics` response payload.
 */
internal object GetLyricsParser {

    /**
     * Parses the `subsonic-response` object into a [LyricsResponse].
     *
     * If the `lyrics` element is missing the response carries an empty [Lyrics].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [LyricsResponse].
     */
    fun parse(json: JSONObject): LyricsResponse {
        val lyrics = json.optJSONObject("lyrics")?.let { obj ->
            Lyrics(
                artist = obj.optStringOrNull("artist"),
                title = obj.optStringOrNull("title"),
                value = obj.optStringOrNull("value")
            )
        } ?: Lyrics()

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
