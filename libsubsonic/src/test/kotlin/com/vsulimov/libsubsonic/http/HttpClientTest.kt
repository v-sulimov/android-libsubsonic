package com.vsulimov.libsubsonic.http

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HttpClientTest {

    private val httpClient = HttpClient()

    @Test
    fun `executeGetRequest returns failure for malformed URL`() {
        val request = GetRequest("not-a-valid-url")
        val result = httpClient.executeGetRequest(request)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `executeGetRequest returns failure for empty URL`() {
        val request = GetRequest("")
        val result = httpClient.executeGetRequest(request)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `openStreamingGetRequest returns failure for malformed URL`() {
        val request = GetRequest("not-a-valid-url")
        val result = httpClient.openStreamingGetRequest(request)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `openStreamingGetRequest returns failure for empty URL`() {
        val request = GetRequest("")
        val result = httpClient.openStreamingGetRequest(request)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `custom timeout values are accepted`() {
        val client = HttpClient(connectTimeoutMs = 5_000, readTimeoutMs = 10_000)
        val request = GetRequest("not-a-valid-url")
        val result = client.executeGetRequest(request)

        assertTrue(result.isFailure)
    }
}
