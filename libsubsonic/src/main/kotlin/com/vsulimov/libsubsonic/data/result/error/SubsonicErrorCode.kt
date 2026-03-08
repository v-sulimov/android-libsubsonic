package com.vsulimov.libsubsonic.data.result.error

/**
 * Enumerates the error codes defined by the Subsonic REST API specification.
 *
 * Each entry corresponds to a well-known error condition that the server may
 * return inside the `error` element of a failed response. The [GENERIC_ERROR]
 * entry is also used for client-side errors such as network failures, parsing
 * errors, and configuration problems.
 *
 * @property code The numeric error code as returned by the Subsonic server.
 */
enum class SubsonicErrorCode(val code: Int) {

    /** A generic or unclassified error (code 0). Also used for client-side errors. */
    GENERIC_ERROR(0),

    /** A required parameter is missing (code 10). */
    MISSING_PARAMETER(10),

    /** Incompatible Subsonic REST API protocol version (code 20). */
    INCOMPATIBLE_VERSION(20),

    /** Wrong username or password — legacy plain-text authentication (code 30). */
    WRONG_CREDENTIALS_LEGACY(30),

    /** Wrong username or password — token-based authentication (code 40). */
    WRONG_CREDENTIALS(40),

    /** Token-based authentication is not supported by the server (code 41). */
    TOKEN_AUTH_NOT_SUPPORTED(41),

    /** The user is not authorized for the requested operation (code 50). */
    NOT_AUTHORIZED(50),

    /** The trial period for the Subsonic server has expired (code 60). */
    TRIAL_EXPIRED(60),

    /** The requested data was not found (code 70). */
    DATA_NOT_FOUND(70);

    companion object {

        /**
         * Returns the [SubsonicErrorCode] matching the given numeric [code],
         * or [GENERIC_ERROR] if the code is not recognized.
         *
         * @param code The numeric error code from the server response.
         * @return The corresponding enum entry.
         */
        fun fromCode(code: Int): SubsonicErrorCode =
            entries.find { it.code == code } ?: GENERIC_ERROR
    }
}
