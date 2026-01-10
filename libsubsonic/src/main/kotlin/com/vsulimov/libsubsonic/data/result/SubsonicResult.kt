package com.vsulimov.libsubsonic.data.result

import com.vsulimov.libsubsonic.data.result.error.SubsonicError

/**
 * A sealed interface representing the result of a Subsonic API operation.
 *
 * @param T The type of data returned on success.
 */
sealed interface SubsonicResult<out T> {

    data class Success<T>(val data: T) : SubsonicResult<T>

    data class Failure(val error: SubsonicError) : SubsonicResult<Nothing>
}
