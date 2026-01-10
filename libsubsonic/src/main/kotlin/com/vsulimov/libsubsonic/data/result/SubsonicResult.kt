package com.vsulimov.libsubsonic.data.result

import com.vsulimov.libsubsonic.data.result.error.SubsonicError

/**
 * Represents the outcome of a Subsonic API operation.
 *
 * @param T The type of data returned when the request succeeds.
 */
sealed interface SubsonicResult<out T> {

    /**
     * A successful API response containing parsed data.
     *
     * @property data The parsed response payload.
     */
    data class Success<T>(val data: T) : SubsonicResult<T>

    /**
     * A failed API response containing the error details.
     *
     * @property error The structured Subsonic error information.
     */
    data class Failure(val error: SubsonicError) : SubsonicResult<Nothing>
}
