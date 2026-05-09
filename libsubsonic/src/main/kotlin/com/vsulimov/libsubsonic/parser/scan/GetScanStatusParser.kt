package com.vsulimov.libsubsonic.parser.scan

import com.vsulimov.libsubsonic.data.response.scan.ScanStatus
import com.vsulimov.libsubsonic.data.response.scan.ScanStatusResponse
import com.vsulimov.libsubsonic.parser.optLongOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `getScanStatus` response payload and provides the shared [parseScanStatus]
 * helper used by [StartScanParser].
 */
internal object GetScanStatusParser {

    /**
     * Parses the `subsonic-response` object into a [ScanStatusResponse].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [ScanStatusResponse].
     * @throws org.json.JSONException If the `scanStatus` element is missing.
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
     * Parses a `scanStatus` JSON object into a [ScanStatus].
     *
     * Shared with [StartScanParser] since both endpoints return the same payload shape.
     *
     * @param scanJson The JSON object representing the scan status.
     * @return The parsed [ScanStatus].
     */
    internal fun parseScanStatus(scanJson: JSONObject) = ScanStatus(
        scanning = scanJson.optBoolean("scanning"),
        count = scanJson.optLongOrNull("count")
    )
}
