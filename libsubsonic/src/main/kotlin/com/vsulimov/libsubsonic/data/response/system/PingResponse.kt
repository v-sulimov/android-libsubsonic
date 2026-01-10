package com.vsulimov.libsubsonic.data.response.system

import com.vsulimov.libsubsonic.data.response.SubsonicResponse

/**
 * Represents the response from a `ping` request, containing server status and metadata.
 *
 * This class captures the basic connectivity and implementation details of the Subsonic server.
 *
 * @property status The status of the response, typically "ok".
 * @property apiVersion The Subsonic REST API version supported by the server (for example, "1.16.1").
 * @property serverType The implementation type of the server (for example, "navidrome" or "subsonic").
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 */
data class PingResponse(
    override val status: String,
    override val apiVersion: String,
    override val serverType: String?,
    override val serverVersion: String?,
    override val isOpenSubsonic: Boolean
) : SubsonicResponse(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
