package com.vsulimov.libsubsonic.data

/**
 * Global constants used throughout the library.
 *
 * This object centralizes default configuration values required by the Subsonic REST API
 * to ensure consistency across different components of the library.
 */
object Constants {

    /**
     * The default identifier for the client application.
     *
     * In the Subsonic API, this is passed as the `c` parameter. It allows server
     * administrators to identify which application is making requests (for example,
     * in logs or active session views).
     */
    const val DEFAULT_CLIENT_NAME = "libsubsonic"

    /**
     * The default Subsonic REST API version used for requests.
     *
     * In the Subsonic API, this is passed as the `v` parameter. This version string
     * informs the server which set of API features and response formats the library
     * expects.
     */
    const val DEFAULT_API_VERSION = "1.16.1"

    /**
     * The default error code used when a more specific error status is unavailable.
     *
     * In the Subsonic API, error codes are typically returned in the `error` element
     * of a failed response. A value of `0` generally represents a generic or
     * "Unknown Error," serving as a fallback for internal processing.
     */
    const val DEFAULT_ERROR_CODE = 0

    /**
     * The default status string used as a fallback when parsing a response
     * that does not include an explicit status field.
     */
    const val DEFAULT_RESPONSE_STATUS = "ok"

    /**
     * The default API version string used as a fallback when parsing a response
     * that does not include an explicit version field.
     */
    const val DEFAULT_VERSION_FALLBACK = "Unknown"
}
