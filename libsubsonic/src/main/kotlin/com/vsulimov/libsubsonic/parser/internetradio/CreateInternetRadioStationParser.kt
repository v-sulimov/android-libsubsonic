package com.vsulimov.libsubsonic.parser.internetradio

import com.vsulimov.libsubsonic.data.response.internetradio.CreateInternetRadioStationResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `createInternetRadioStation` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object CreateInternetRadioStationParser {

    /**
     * Parses the "subsonic-response" object into a [CreateInternetRadioStationResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [CreateInternetRadioStationResponse].
     */
    fun parse(json: JSONObject): CreateInternetRadioStationResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return CreateInternetRadioStationResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
