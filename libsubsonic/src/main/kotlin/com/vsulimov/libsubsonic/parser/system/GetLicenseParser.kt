package com.vsulimov.libsubsonic.parser.system

import com.vsulimov.libsubsonic.data.response.system.License
import com.vsulimov.libsubsonic.data.response.system.LicenseResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `getLicense` response payload.
 */
internal object GetLicenseParser {

    /**
     * Parses the "subsonic-response" object into a [LicenseResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [LicenseResponse].
     */
    fun parse(json: JSONObject): LicenseResponse {
        val licenseObj = json.optJSONObject("license")
        val license = if (licenseObj != null) {
            License(
                valid = licenseObj.optBoolean("valid", false),
                email = licenseObj.optString("email").ifEmpty { null },
                licenseExpires = licenseObj.optString("licenseExpires").ifEmpty { null }
            )
        } else {
            License(valid = false)
        }

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return LicenseResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            license = license
        )
    }
}
