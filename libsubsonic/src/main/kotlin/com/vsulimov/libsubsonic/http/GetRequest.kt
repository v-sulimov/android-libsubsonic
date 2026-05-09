package com.vsulimov.libsubsonic.http

/**
 * A single HTTP GET request, identified by its fully qualified URL.
 *
 * @property url The fully qualified URL (including query string) to send the request to.
 */
internal data class GetRequest(val url: String)
