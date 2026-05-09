package com.vsulimov.libsubsonic.parser.annotation

import com.vsulimov.libsubsonic.data.response.annotation.SetRatingResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `setRating` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object SetRatingParser {

    /**
     * Parses the `subsonic-response` object into a [SetRatingResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [SetRatingResponse].
     */
    fun parse(json: JSONObject): SetRatingResponse = json.envelopeOnly(::SetRatingResponse)
}
