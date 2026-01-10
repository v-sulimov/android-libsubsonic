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
 * This class is responsible for the execution of Subsonic API requests.
 *
 * It bridges the synchronous [HttpClient] with Kotlin Coroutines, handling
 * thread switching, URL generation, network execution, and response parsing.
 *
 * @property urlBuilder The utility to construct signed URLs.
 * @property responseParser The utility to parse JSON responses.
 */
internal class SubsonicRequestExecutor(
    private val urlBuilder: SubsonicUrlBuilder,
    private val responseParser: SubsonicResponseParser = SubsonicResponseParser()
) {

    // The low-level HTTP client.
    private val httpClient = HttpClient()

    /**
     * Executes a GET request against the Subsonic API.
     *
     * This method is main-safe. It suspends execution and moves blocking network I/O
     * to [Dispatchers.IO].
     *
     * @param endpoint The API endpoint to call (e.g., "ping").
     * @param params Optional query parameters to append to the request.
     * @param dataParser A lambda to extract the specific data from the JSON response.
     * @return A [SubsonicResult] representing success or failure.
     */
    suspend fun <T> execute(
        endpoint: String,
        params: Map<String, String> = emptyMap(),
        dataParser: (JSONObject) -> T
    ): SubsonicResult<T> = withContext(Dispatchers.IO) {
        try {
            // Build signed URL
            val url = urlBuilder.buildUrl(endpoint, params)

            // Execute Network Request
            val request = GetRequest(url)
            val networkResult = httpClient.executeGetRequest(request)

            // Handle Network Result
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
            // Usually credential errors from UrlBuilder
            SubsonicResult.Failure(
                SubsonicError(
                    code = DEFAULT_ERROR_CODE,
                    message = e.message ?: "Configuration error"
                )
            )
        } catch (e: Exception) {
            // Catch-all for unexpected runtime errors
            SubsonicResult.Failure(
                SubsonicError(
                    code = DEFAULT_ERROR_CODE,
                    message = e.message ?: "Unexpected client error"
                )
            )
        }
    }
}
