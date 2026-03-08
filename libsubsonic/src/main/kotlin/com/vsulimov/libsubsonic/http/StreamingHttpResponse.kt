package com.vsulimov.libsubsonic.http

import java.io.Closeable
import java.io.InputStream
import java.net.HttpURLConnection

/**
 * Represents an HTTP response whose body is available as a raw byte stream.
 *
 * The [body] stream remains valid until [close] is called. The caller must
 * close this response when finished to release the underlying connection.
 *
 * @property body The response body as an [InputStream] for direct consumption.
 * @property connection The underlying HTTP connection, kept open until [close] is called.
 */
class StreamingHttpResponse internal constructor(
    val body: InputStream,
    private val connection: HttpURLConnection
) : Closeable {

    /**
     * Closes the underlying HTTP connection and releases associated resources.
     */
    override fun close() {
        connection.disconnect()
    }
}
