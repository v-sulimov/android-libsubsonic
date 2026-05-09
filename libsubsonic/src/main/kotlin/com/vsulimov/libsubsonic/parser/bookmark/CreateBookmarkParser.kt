package com.vsulimov.libsubsonic.parser.bookmark

import com.vsulimov.libsubsonic.data.response.bookmark.CreateBookmarkResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `createBookmark` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object CreateBookmarkParser {

    /**
     * Parses the `subsonic-response` object into a [CreateBookmarkResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [CreateBookmarkResponse].
     */
    fun parse(json: JSONObject): CreateBookmarkResponse = json.envelopeOnly(::CreateBookmarkResponse)
}
