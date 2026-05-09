package com.vsulimov.libsubsonic.parser.internetradio

import com.vsulimov.libsubsonic.data.response.internetradio.UpdateInternetRadioStationResponse
import com.vsulimov.libsubsonic.parser.envelopeOnly
import org.json.JSONObject

/**
 * Parses the `updateInternetRadioStation` response payload.
 *
 * The server returns an empty envelope on success, so only the standard envelope fields
 * are extracted.
 */
internal object UpdateInternetRadioStationParser {

    /**
     * Parses the `subsonic-response` object into an [UpdateInternetRadioStationResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [UpdateInternetRadioStationResponse].
     */
    fun parse(json: JSONObject): UpdateInternetRadioStationResponse =
        json.envelopeOnly(::UpdateInternetRadioStationResponse)
}
