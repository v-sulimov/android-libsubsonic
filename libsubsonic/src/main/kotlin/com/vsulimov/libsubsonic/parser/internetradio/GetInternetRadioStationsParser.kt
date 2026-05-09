package com.vsulimov.libsubsonic.parser.internetradio

import com.vsulimov.libsubsonic.data.response.internetradio.InternetRadioStation
import com.vsulimov.libsubsonic.data.response.internetradio.InternetRadioStationsResponse
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getInternetRadioStations` response payload.
 */
internal object GetInternetRadioStationsParser {

    /**
     * Parses the `subsonic-response` object into an [InternetRadioStationsResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [InternetRadioStationsResponse].
     */
    fun parse(json: JSONObject): InternetRadioStationsResponse {
        val stations = json.optJSONObject("internetRadioStations")
            ?.parseList("internetRadioStation") { stationJson ->
                InternetRadioStation(
                    id = stationJson.optString("id"),
                    name = stationJson.optString("name"),
                    streamUrl = stationJson.optString("streamUrl"),
                    homePageUrl = stationJson.optStringOrNull("homePageUrl")
                )
            }
            .orEmpty()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return InternetRadioStationsResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            stations = stations
        )
    }
}
