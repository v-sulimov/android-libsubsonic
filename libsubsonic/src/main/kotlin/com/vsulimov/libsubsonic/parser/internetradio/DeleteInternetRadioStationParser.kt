package com.vsulimov.libsubsonic.parser.internetradio

import com.vsulimov.libsubsonic.data.response.internetradio.DeleteInternetRadioStationResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `deleteInternetRadioStation` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object DeleteInternetRadioStationParser {

    /**
     * Parses the `subsonic-response` object into a [DeleteInternetRadioStationResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [DeleteInternetRadioStationResponse].
     */
    fun parse(json: JSONObject): DeleteInternetRadioStationResponse =
        json.envelopeOnly(::DeleteInternetRadioStationResponse)
}
