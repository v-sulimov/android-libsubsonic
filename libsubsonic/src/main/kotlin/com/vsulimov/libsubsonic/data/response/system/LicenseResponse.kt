package com.vsulimov.libsubsonic.data.response.system

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getLicense` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property license The license information returned by the server.
 */
data class LicenseResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val license: License
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
