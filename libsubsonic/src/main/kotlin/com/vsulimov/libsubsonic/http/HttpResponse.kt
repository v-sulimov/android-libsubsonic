package com.vsulimov.libsubsonic.http

/**
 * A completed HTTP response with the body fully read into memory as a UTF-8 string.
 *
 * @property body The response body content, decoded as UTF-8.
 */
internal data class HttpResponse(val body: String)
