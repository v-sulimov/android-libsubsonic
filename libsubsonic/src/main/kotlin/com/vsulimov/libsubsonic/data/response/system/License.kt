package com.vsulimov.libsubsonic.data.response.system

/**
 * Represents the license information returned by the Subsonic server.
 *
 * @property valid Indicates whether the server license is valid.
 * @property email The email address associated with the license.
 * @property licenseExpires The ISO 8601 timestamp indicating when the license expires.
 */
data class License(val valid: Boolean, val email: String? = null, val licenseExpires: String? = null)
