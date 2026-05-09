package com.vsulimov.libsubsonic.parser.bookmark

import com.vsulimov.libsubsonic.data.response.bookmark.DeleteBookmarkResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `deleteBookmark` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object DeleteBookmarkParser {

    /**
     * Parses the `subsonic-response` object into a [DeleteBookmarkResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [DeleteBookmarkResponse].
     */
    fun parse(json: JSONObject): DeleteBookmarkResponse = json.envelopeOnly(::DeleteBookmarkResponse)
}
