package com.vsulimov.libsubsonic.http

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * A lightweight HTTP client backed by [HttpURLConnection].
 *
 * Supports standard GET requests that read the response body into memory and
 * streaming GET requests that deliver the response body as an
 * [java.io.InputStream] for on-the-fly consumption. Connections are pooled
 * automatically by the underlying [HttpURLConnection] implementation.
 *
 * @property connectTimeoutMs Maximum time in milliseconds to establish a connection.
 * @property readTimeoutMs Maximum time in milliseconds to wait for data during a read.
 */
class HttpClient(
    private val connectTimeoutMs: Int = DEFAULT_CONNECT_TIMEOUT_MS,
    private val readTimeoutMs: Int = DEFAULT_READ_TIMEOUT_MS
) {

    /**
     * Executes a GET request and reads the entire response body into memory.
     *
     * @param request The [GetRequest] containing the target URL.
     * @return [Result.success] with an [HttpResponse] on a 200 OK response,
     *   or [Result.failure] wrapping the encountered exception on any error.
     */
    fun executeGetRequest(request: GetRequest): Result<HttpResponse> {
        return try {
            val connection = openConnection(request.url)
            try {
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val body = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                    Result.success(HttpResponse(body))
                } else {
                    Result.failure(IOException("HTTP $responseCode: ${connection.responseMessage}"))
                }
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Opens a streaming GET connection and returns the response for direct consumption.
     *
     * The caller is responsible for closing the returned [StreamingHttpResponse] when
     * finished to release the underlying connection.
     *
     * @param request The [GetRequest] containing the target URL.
     * @return [Result.success] with a [StreamingHttpResponse] on a 200 OK response,
     *   or [Result.failure] wrapping the encountered exception on any error.
     */
    fun openStreamingGetRequest(request: GetRequest): Result<StreamingHttpResponse> {
        return try {
            val connection = openConnection(request.url)
            try {
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Result.success(StreamingHttpResponse(connection.inputStream, connection))
                } else {
                    connection.disconnect()
                    Result.failure(IOException("HTTP $responseCode: ${connection.responseMessage}"))
                }
            } catch (e: Exception) {
                connection.disconnect()
                throw e
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Opens and configures an [HttpURLConnection] for a GET request.
     *
     * @param url The fully-qualified URL to connect to.
     * @return A configured [HttpURLConnection] ready for use.
     */
    private fun openConnection(url: String): HttpURLConnection {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = connectTimeoutMs
        connection.readTimeout = readTimeoutMs
        return connection
    }

    companion object {
        /** Default connection timeout: 15 seconds. */
        const val DEFAULT_CONNECT_TIMEOUT_MS = 15_000

        /** Default read timeout: 30 seconds. */
        const val DEFAULT_READ_TIMEOUT_MS = 30_000
    }
}
