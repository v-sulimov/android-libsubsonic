package com.vsulimov.libsubsonic.parser.internetradio

import com.vsulimov.libsubsonic.data.response.internetradio.UpdateInternetRadioStationResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `updateInternetRadioStation` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object UpdateInternetRadioStationParser {

    /**
     * Parses the "subsonic-response" object into an [UpdateInternetRadioStationResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [UpdateInternetRadioStationResponse].
     */
    fun parse(json: JSONObject): UpdateInternetRadioStationResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return UpdateInternetRadioStationResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
