package com.vsulimov.libsubsonic.http

/**
 * Represents an HTTP GET request.
 *
 * @property url The fully qualified URL to send the request to.
 */
data class GetRequest(val url: String)
