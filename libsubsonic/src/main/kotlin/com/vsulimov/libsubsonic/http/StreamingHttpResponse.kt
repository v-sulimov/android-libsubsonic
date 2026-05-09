package com.vsulimov.libsubsonic.http

import java.io.Closeable
import java.io.InputStream
import java.net.HttpURLConnection

/**
 * An HTTP response whose body is exposed as a raw byte stream rather than buffered into memory.
 *
 * The underlying connection is held open until [close] is called, so callers must always
 * close this response (typically via `use { ... }`) once they have finished reading [body].
 *
 * @property body The response body, available as an [InputStream] for direct consumption.
 */
internal class StreamingHttpResponse(val body: InputStream, private val connection: HttpURLConnection) : Closeable {

    /** Disconnects the underlying [HttpURLConnection] and releases its resources. */
    override fun close() {
        connection.disconnect()
    }
}
