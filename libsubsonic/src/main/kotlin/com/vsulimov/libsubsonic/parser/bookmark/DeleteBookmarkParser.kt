package com.vsulimov.libsubsonic.parser.bookmark

import com.vsulimov.libsubsonic.data.response.bookmark.DeleteBookmarkResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `deleteBookmark` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object DeleteBookmarkParser {

    /**
     * Parses the "subsonic-response" object into a [DeleteBookmarkResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [DeleteBookmarkResponse].
     */
    fun parse(json: JSONObject): DeleteBookmarkResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return DeleteBookmarkResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
