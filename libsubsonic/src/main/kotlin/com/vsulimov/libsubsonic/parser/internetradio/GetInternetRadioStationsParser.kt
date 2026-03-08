package com.vsulimov.libsubsonic.parser.internetradio

import com.vsulimov.libsubsonic.data.response.internetradio.InternetRadioStation
import com.vsulimov.libsubsonic.data.response.internetradio.InternetRadioStationsResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getInternetRadioStations` response payload.
 */
internal object GetInternetRadioStationsParser {

    /**
     * Parses the "subsonic-response" object into an [InternetRadioStationsResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [InternetRadioStationsResponse].
     */
    fun parse(json: JSONObject): InternetRadioStationsResponse {
        val containerObj = json.optJSONObject("internetRadioStations")
        val stations = containerObj?.parseList("internetRadioStation") { stationJson ->
            InternetRadioStation(
                id = stationJson.optString("id"),
                name = stationJson.optString("name"),
                streamUrl = stationJson.optString("streamUrl"),
                homePageUrl = stationJson.optString("homePageUrl").ifEmpty { null }
            )
        } ?: emptyList()

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
