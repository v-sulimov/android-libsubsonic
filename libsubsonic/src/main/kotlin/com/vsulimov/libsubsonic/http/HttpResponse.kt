package com.vsulimov.libsubsonic.http

/**
 * Represents a completed HTTP response with the body read as a string.
 *
 * @property body The response body content as a UTF-8 string.
 */
data class HttpResponse(val body: String)
