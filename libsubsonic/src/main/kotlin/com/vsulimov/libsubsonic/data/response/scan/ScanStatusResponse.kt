package com.vsulimov.libsubsonic.data.response.scan

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `getScanStatus` request.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 * @property scanStatus The current scan status of the media library.
 */
data class ScanStatusResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean,
    val scanStatus: ScanStatus
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
