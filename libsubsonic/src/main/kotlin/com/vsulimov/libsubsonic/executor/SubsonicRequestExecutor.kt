package com.vsulimov.libsubsonic.executor

import com.vsulimov.httpclient.HttpClient
import com.vsulimov.httpclient.request.GetRequest
import com.vsulimov.libsubsonic.data.Constants.DEFAULT_ERROR_CODE
import com.vsulimov.libsubsonic.data.result.SubsonicResult
import com.vsulimov.libsubsonic.data.result.error.SubsonicError
import com.vsulimov.libsubsonic.parser.SubsonicResponseParser
import com.vsulimov.libsubsonic.url.SubsonicUrlBuilder
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
 * @property responseParser The parser used to decode the Subsonic response envelope.
 */
internal class SubsonicRequestExecutor(
    private val urlBuilder: SubsonicUrlBuilder,
    private val responseParser: SubsonicResponseParser = SubsonicResponseParser()
) {

    private val httpClient = HttpClient()

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
                            code = DEFAULT_ERROR_CODE,
                            message = exception.message ?: "Network transport error"
                        )
                    )
                }
            )
        } catch (e: IllegalStateException) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = DEFAULT_ERROR_CODE,
                    message = e.message ?: "Configuration error"
                )
            )
        } catch (e: Exception) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = DEFAULT_ERROR_CODE,
                    message = e.message ?: "Unexpected client error"
                )
            )
        }
    }
}
