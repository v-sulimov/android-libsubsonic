package com.vsulimov.libsubsonic.parser.scan

import com.vsulimov.libsubsonic.data.response.scan.ScanStatusResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `startScan` response payload.
 */
internal object StartScanParser {

    /**
     * Parses the "subsonic-response" object into a [ScanStatusResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [ScanStatusResponse].
     */
    fun parse(json: JSONObject): ScanStatusResponse {
        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return ScanStatusResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            scanStatus = GetScanStatusParser.parseScanStatus(json.getJSONObject("scanStatus"))
        )
    }
}
