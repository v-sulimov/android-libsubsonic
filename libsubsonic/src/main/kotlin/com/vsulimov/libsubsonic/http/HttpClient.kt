package com.vsulimov.libsubsonic.http

import com.vsulimov.libsubsonic.data.Constants.DEFAULT_CONNECT_TIMEOUT_MS
import com.vsulimov.libsubsonic.data.Constants.DEFAULT_READ_TIMEOUT_MS
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * A lightweight HTTP client backed by [HttpURLConnection].
 *
 * Supports two modes:
 * - [executeGetRequest] reads the response body fully into memory and returns it as an
 *   [HttpResponse]; the connection is always closed before the call returns.
 * - [openStreamingGetRequest] hands the response back as a [StreamingHttpResponse] without
 *   buffering, leaving the connection open until the caller closes the response.
 *
 * Connection pooling is handled automatically by [HttpURLConnection].
 *
 * @property connectTimeoutMs Maximum time, in milliseconds, allowed to establish a connection.
 * @property readTimeoutMs Maximum time, in milliseconds, allowed to wait for data during a read.
 */
internal class HttpClient(
    private val connectTimeoutMs: Int = DEFAULT_CONNECT_TIMEOUT_MS,
    private val readTimeoutMs: Int = DEFAULT_READ_TIMEOUT_MS
) {

    /**
     * Executes a GET request and reads the entire response body into memory.
     *
     * @param request The [GetRequest] containing the target URL.
     * @return [Result.success] with an [HttpResponse] on a 200 OK response, or
     *   [Result.failure] wrapping the encountered exception on any error.
     */
    fun executeGetRequest(request: GetRequest): Result<HttpResponse> = runCatching {
        val connection = openConnection(request.url)
        try {
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw httpStatusError(connection)
            }
            val body = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            HttpResponse(body)
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Opens a streaming GET connection and returns the response for direct consumption.
     *
     * The caller is responsible for closing the returned [StreamingHttpResponse] when finished
     * to release the underlying connection.
     *
     * @param request The [GetRequest] containing the target URL.
     * @return [Result.success] with a [StreamingHttpResponse] on a 200 OK response, or
     *   [Result.failure] wrapping the encountered exception on any error.
     */
    fun openStreamingGetRequest(request: GetRequest): Result<StreamingHttpResponse> = runCatching {
        val connection = openConnection(request.url)
        try {
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw httpStatusError(connection)
            }
            StreamingHttpResponse(connection.inputStream, connection)
        } catch (e: Throwable) {
            connection.disconnect()
            throw e
        }
    }

    private fun openConnection(url: String): HttpURLConnection =
        (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = connectTimeoutMs
            readTimeout = readTimeoutMs
        }

    private fun httpStatusError(connection: HttpURLConnection): IOException =
        IOException("HTTP ${connection.responseCode}: ${connection.responseMessage}")
}
