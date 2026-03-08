package com.vsulimov.libsubsonic.parser.internetradio

import com.vsulimov.libsubsonic.data.response.internetradio.DeleteInternetRadioStationResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `deleteInternetRadioStation` response payload.
 *
 * The server returns an empty envelope on success, so only the standard
 * metadata fields are extracted.
 */
internal object DeleteInternetRadioStationParser {

    /**
     * Parses the "subsonic-response" object into a [DeleteInternetRadioStationResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [DeleteInternetRadioStationResponse].
     */
    fun parse(json: JSONObject): DeleteInternetRadioStationResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return DeleteInternetRadioStationResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic
        )
    }
}
