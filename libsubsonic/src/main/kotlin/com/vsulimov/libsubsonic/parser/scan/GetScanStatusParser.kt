package com.vsulimov.libsubsonic.parser.scan

import com.vsulimov.libsubsonic.data.response.scan.ScanStatus
import com.vsulimov.libsubsonic.data.response.scan.ScanStatusResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `getScanStatus` response payload.
 */
internal object GetScanStatusParser {

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
            scanStatus = parseScanStatus(json.getJSONObject("scanStatus"))
        )
    }

    /**
     * Parses a scan status JSON object into a [ScanStatus].
     *
     * This function is shared with [StartScanParser] which uses the same response structure.
     *
     * @param scanJson The JSON object representing the scan status.
     * @return The parsed [ScanStatus].
     */
    internal fun parseScanStatus(scanJson: JSONObject): ScanStatus = ScanStatus(
        scanning = scanJson.optBoolean("scanning"),
        count = if (scanJson.has("count")) scanJson.optLong("count") else null
    )
}
