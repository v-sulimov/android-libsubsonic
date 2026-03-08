package com.vsulimov.libsubsonic.data.response

/**
 * Base class for all Subsonic API responses.
 *
 * This class captures the common metadata fields present in the "subsonic-response"
 * JSON envelope returned by every successful Subsonic API endpoint.
 *
 * @property status The status of the response, typically "ok" for success.
 * @property apiVersion The Subsonic REST API version supported by the server (for example, "1.16.1").
 * @property serverType The implementation type of the server (for example, "navidrome" or "subsonic").
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 */
abstract class SubsonicResponse(
    open val status: String,
    open val apiVersion: String,
    open val serverType: String?,
    open val serverVersion: String?,
    open val isOpenSubsonic: Boolean
)
