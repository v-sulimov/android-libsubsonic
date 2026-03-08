package com.vsulimov.libsubsonic.parser.bookmark

import com.vsulimov.libsubsonic.data.response.bookmark.CreateBookmarkResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `createBookmark` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object CreateBookmarkParser {

    /**
     * Parses the "subsonic-response" object into a [CreateBookmarkResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [CreateBookmarkResponse].
     */
    fun parse(json: JSONObject): CreateBookmarkResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return CreateBookmarkResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
