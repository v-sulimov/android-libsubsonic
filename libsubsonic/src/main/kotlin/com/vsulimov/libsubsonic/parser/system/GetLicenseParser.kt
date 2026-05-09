package com.vsulimov.libsubsonic.parser.system

import com.vsulimov.libsubsonic.data.response.system.License
import com.vsulimov.libsubsonic.data.response.system.LicenseResponse
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import org.json.JSONObject

/**
 * Parses the `getLicense` response payload.
 */
internal object GetLicenseParser {

    /**
     * Parses the `subsonic-response` object into a [LicenseResponse].
     *
     * If the `license` element is missing the response carries a default `License(valid = false)`.
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [LicenseResponse].
     */
    fun parse(json: JSONObject): LicenseResponse {
        val license = json.optJSONObject("license")?.let { obj ->
            License(
                valid = obj.optBoolean("valid", false),
                email = obj.optStringOrNull("email"),
                licenseExpires = obj.optStringOrNull("licenseExpires")
            )
        } ?: License(valid = false)

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
