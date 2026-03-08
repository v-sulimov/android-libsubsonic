package com.vsulimov.libsubsonic.executor

import com.vsulimov.libsubsonic.data.result.SubsonicResult
import com.vsulimov.libsubsonic.data.result.error.SubsonicError
import com.vsulimov.libsubsonic.data.result.error.SubsonicErrorCode
import com.vsulimov.libsubsonic.http.GetRequest
import com.vsulimov.libsubsonic.http.HttpClient
import com.vsulimov.libsubsonic.parser.SubsonicResponseParser
import com.vsulimov.libsubsonic.url.SubsonicUrlBuilder
import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Executes Subsonic API requests and maps responses to [SubsonicResult] values.
 *
 * This component bridges the synchronous [HttpClient] with Kotlin coroutines by
 * performing blocking I/O on [Dispatchers.IO], building signed URLs, and delegating
 * JSON parsing to [SubsonicResponseParser].
 *
 * @property urlBuilder The utility used to construct signed URLs.
 * @property httpClient The HTTP client used to perform network requests.
 * @property responseParser The parser used to decode the Subsonic response envelope.
 */
internal class SubsonicRequestExecutor(
    private val urlBuilder: SubsonicUrlBuilder,
    private val httpClient: HttpClient = HttpClient(),
    private val responseParser: SubsonicResponseParser = SubsonicResponseParser()
) {

    /**
     * Executes a streaming GET request against the Subsonic API.
     *
     * This method is main-safe. It suspends execution and performs blocking network I/O
     * on [Dispatchers.IO]. The [responseHandler] receives the raw response body as an
     * [InputStream] for direct consumption.
     *
     * @param endpoint The API endpoint to call (e.g., "stream.view").
     * @param params Optional single-value query parameters to append to the request.
     * @param multiValueParams Optional multi-value query parameters where a single key maps to
     *   a list of values, each appended as a separate query pair (e.g., `bitRate=128&bitRate=320`).
     * @param responseHandler A suspend lambda that consumes the [InputStream] response body.
     * @return A [SubsonicResult] representing success or failure.
     */
    suspend fun executeStreaming(
        endpoint: String,
        params: Map<String, String> = emptyMap(),
        multiValueParams: Map<String, List<String>> = emptyMap(),
        responseHandler: suspend (InputStream) -> Unit
    ): SubsonicResult<Unit> = withContext(Dispatchers.IO) {
        try {
            val url = urlBuilder.buildUrl(endpoint, params, multiValueParams)
            val request = GetRequest(url)
            val networkResult = httpClient.openStreamingGetRequest(request)

            networkResult.fold(
                onSuccess = { response ->
                    response.use { responseHandler(it.body) }
                    SubsonicResult.Success(Unit)
                },
                onFailure = { exception ->
                    SubsonicResult.Failure(
                        SubsonicError(
                            code = SubsonicErrorCode.GENERIC_ERROR,
                            message = exception.message ?: "Network transport error"
                        )
                    )
                }
            )
        } catch (e: IllegalStateException) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = SubsonicErrorCode.GENERIC_ERROR,
                    message = e.message ?: "Configuration error"
                )
            )
        } catch (e: Exception) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = SubsonicErrorCode.GENERIC_ERROR,
                    message = e.message ?: "Unexpected client error"
                )
            )
        }
    }

    /**
     * Executes a GET request against the Subsonic API.
     *
     * This method is main-safe. It suspends execution and performs blocking network I/O
     * on [Dispatchers.IO].
     *
     * @param endpoint The API endpoint to call (e.g., "ping").
     * @param params Optional single-value query parameters to append to the request.
     * @param multiValueParams Optional multi-value query parameters where a single key maps to
     *   a list of values, each appended as a separate query pair (e.g., `songId=1&songId=2`).
     * @param dataParser A lambda to extract the specific data from the JSON response.
     * @return A [SubsonicResult] representing success or failure.
     */
    suspend fun <T> execute(
        endpoint: String,
        params: Map<String, String> = emptyMap(),
        multiValueParams: Map<String, List<String>> = emptyMap(),
        dataParser: (JSONObject) -> T
    ): SubsonicResult<T> = withContext(Dispatchers.IO) {
        try {
            val url = urlBuilder.buildUrl(endpoint, params, multiValueParams)
            val request = GetRequest(url)
            val networkResult = httpClient.executeGetRequest(request)

            networkResult.fold(
                onSuccess = { response ->
                    responseParser.parse(response.body, dataParser)
                },
                onFailure = { exception ->
                    SubsonicResult.Failure(
                        SubsonicError(
                            code = SubsonicErrorCode.GENERIC_ERROR,
                            message = exception.message ?: "Network transport error"
                        )
                    )
                }
            )
        } catch (e: IllegalStateException) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = SubsonicErrorCode.GENERIC_ERROR,
                    message = e.message ?: "Configuration error"
                )
            )
        } catch (e: Exception) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = SubsonicErrorCode.GENERIC_ERROR,
                    message = e.message ?: "Unexpected client error"
                )
            )
        }
    }
}
