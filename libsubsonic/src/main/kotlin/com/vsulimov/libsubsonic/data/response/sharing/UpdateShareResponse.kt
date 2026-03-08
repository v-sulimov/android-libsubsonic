package com.vsulimov.libsubsonic.data.response.sharing

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from an `updateShare` request.
 *
 * The server returns an empty envelope on success — no domain-specific payload is included.
 *
 * @property status The status of the response, typically "ok".
 * @property apiVersion The Subsonic REST API version supported by the server.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 */
data class UpdateShareResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
