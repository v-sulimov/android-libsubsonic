package com.vsulimov.libsubsonic.parser.internetradio

import com.vsulimov.libsubsonic.data.response.internetradio.CreateInternetRadioStationResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `createInternetRadioStation` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object CreateInternetRadioStationParser {

    /**
     * Parses the `subsonic-response` object into a [CreateInternetRadioStationResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [CreateInternetRadioStationResponse].
     */
    fun parse(json: JSONObject): CreateInternetRadioStationResponse =
        json.envelopeOnly(::CreateInternetRadioStationResponse)
}
