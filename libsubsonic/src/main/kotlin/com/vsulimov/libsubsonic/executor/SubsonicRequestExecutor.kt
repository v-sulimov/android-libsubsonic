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
 * Executes Subsonic API requests and maps every outcome to a [SubsonicResult].
 *
 * Bridges the synchronous [HttpClient] with Kotlin coroutines by running blocking I/O on
 * [Dispatchers.IO], builds signed URLs via [SubsonicUrlBuilder] and delegates JSON envelope
 * parsing to [SubsonicResponseParser].
 *
 * @property urlBuilder Builds signed Subsonic API URLs.
 * @property httpClient Performs the underlying GET requests.
 * @property responseParser Decodes the standard `subsonic-response` envelope.
 */
internal class SubsonicRequestExecutor(
    private val urlBuilder: SubsonicUrlBuilder,
    private val httpClient: HttpClient = HttpClient(),
    private val responseParser: SubsonicResponseParser = SubsonicResponseParser()
) {

    /**
     * Executes a streaming GET request against the Subsonic API.
     *
     * Main-safe: suspends and runs blocking network I/O on [Dispatchers.IO]. The
     * [responseHandler] receives the raw response body as an [InputStream] for direct
     * consumption; the underlying connection is held open until the handler returns.
     *
     * @param endpoint The API endpoint to call (e.g. `"stream.view"`).
     * @param params Single-value query parameters to append to the request.
     * @param multiValueParams Multi-value query parameters where one key maps to many values
     *   (e.g. `bitRate=128&bitRate=320`).
     * @param responseHandler Suspending lambda that consumes the response body stream.
     * @return [SubsonicResult.Success] of [Unit] if the request and handler completed,
     *   [SubsonicResult.Failure] otherwise.
     */
    suspend fun executeStreaming(
        endpoint: String,
        params: Map<String, String> = emptyMap(),
        multiValueParams: Map<String, List<String>> = emptyMap(),
        responseHandler: suspend (InputStream) -> Unit
    ): SubsonicResult<Unit> = runRequest {
        val url = urlBuilder.buildUrl(endpoint, params, multiValueParams)
        httpClient.openStreamingGetRequest(GetRequest(url)).fold(
            onSuccess = { response ->
                response.use { responseHandler(it.body) }
                SubsonicResult.Success(Unit)
            },
            onFailure = { networkFailure(it) }
        )
    }

    /**
     * Executes a GET request against the Subsonic API and parses the JSON response body.
     *
     * Main-safe: suspends and runs blocking network I/O on [Dispatchers.IO].
     *
     * @param endpoint The API endpoint to call (e.g. `"ping.view"`).
     * @param params Single-value query parameters to append to the request.
     * @param multiValueParams Multi-value query parameters where one key maps to many values
     *   (e.g. `songId=1&songId=2`).
     * @param dataParser Lambda that extracts the typed payload from the JSON response body.
     * @return [SubsonicResult.Success] containing the parsed payload, or
     *   [SubsonicResult.Failure] on network, HTTP, or parsing errors.
     */
    suspend fun <T> execute(
        endpoint: String,
        params: Map<String, String> = emptyMap(),
        multiValueParams: Map<String, List<String>> = emptyMap(),
        dataParser: (JSONObject) -> T
    ): SubsonicResult<T> = runRequest {
        val url = urlBuilder.buildUrl(endpoint, params, multiValueParams)
        httpClient.executeGetRequest(GetRequest(url)).fold(
            onSuccess = { response -> responseParser.parse(response.body, dataParser) },
            onFailure = { networkFailure(it) }
        )
    }

    /**
     * Runs [block] on [Dispatchers.IO] and converts unexpected exceptions into
     * [SubsonicResult.Failure] so no exception ever escapes the executor.
     */
    private suspend fun <T> runRequest(block: suspend () -> SubsonicResult<T>): SubsonicResult<T> =
        withContext(Dispatchers.IO) {
            try {
                block()
            } catch (e: IllegalStateException) {
                clientFailure(e, fallback = "Configuration error")
            } catch (e: Exception) {
                clientFailure(e, fallback = "Unexpected client error")
            }
        }

    private fun networkFailure(cause: Throwable): SubsonicResult.Failure = SubsonicResult.Failure(
        SubsonicError(
            code = SubsonicErrorCode.GENERIC_ERROR,
            message = cause.message ?: "Network transport error"
        )
    )

    private fun clientFailure(cause: Throwable, fallback: String): SubsonicResult.Failure = SubsonicResult.Failure(
        SubsonicError(
            code = SubsonicErrorCode.GENERIC_ERROR,
            message = cause.message ?: fallback
        )
    )
}
