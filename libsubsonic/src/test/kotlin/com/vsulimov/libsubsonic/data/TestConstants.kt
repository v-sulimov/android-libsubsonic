package com.vsulimov.libsubsonic.data

/**
 * Shared constants used across unit tests for the Subsonic client and authenticator.
 *
 * These values reflect the default configuration of the library and are used to
 * construct requests and verify authentication parameters in tests.
 */
object TestConstants {

    /** The default client name sent with every Subsonic API request. */
    const val DEFAULT_CLIENT_NAME = "libsubsonic"

    /** The default Subsonic REST API version used by the library. */
    const val DEFAULT_API_VERSION = "1.16.1"

    /** Test username for authenticator and URL builder tests. */
    const val USERNAME = "joe"

    /** Test password for authenticator tests. */
    const val PASSWORD = "sesame"
}
