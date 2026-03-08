package com.vsulimov.libsubsonic.executor

import com.vsulimov.libsubsonic.data.result.SubsonicResult
import com.vsulimov.libsubsonic.data.result.error.SubsonicErrorCode
import com.vsulimov.libsubsonic.url.SubsonicUrlBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

class SubsonicRequestExecutorTest {

    @Test
    fun `execute returns failure when credentials are not set`() = runBlocking {
        val urlBuilder = SubsonicUrlBuilder("http://example.com", "test")
        val executor = SubsonicRequestExecutor(urlBuilder)

        val result = executor.execute("ping.view") { it }

        assertTrue(result is SubsonicResult.Failure)
        assertEquals(SubsonicErrorCode.GENERIC_ERROR, result.error.code)
        assertTrue(result.error.message.contains("Credentials not set"))
    }

    @Test
    fun `executeStreaming returns failure when credentials are not set`() = runBlocking {
        val urlBuilder = SubsonicUrlBuilder("http://example.com", "test")
        val executor = SubsonicRequestExecutor(urlBuilder)
        var handlerCalled = false

        val result = executor.executeStreaming("stream.view") { handlerCalled = true }

        assertTrue(result is SubsonicResult.Failure)
        assertEquals(SubsonicErrorCode.GENERIC_ERROR, result.error.code)
        assertTrue(result.error.message.contains("Credentials not set"))
        assertTrue(!handlerCalled)
    }

    @Test
    fun `execute returns failure for unreachable host`() = runBlocking {
        val urlBuilder = SubsonicUrlBuilder("http://192.0.2.1", "test")
        urlBuilder.setCredentials("user", "pass")
        val executor = SubsonicRequestExecutor(urlBuilder)

        val result = executor.execute("ping.view") { it }

        assertTrue(result is SubsonicResult.Failure)
        assertEquals(SubsonicErrorCode.GENERIC_ERROR, result.error.code)
    }

    @Test
    fun `executeStreaming returns failure for unreachable host`() = runBlocking {
        val urlBuilder = SubsonicUrlBuilder("http://192.0.2.1", "test")
        urlBuilder.setCredentials("user", "pass")
        val executor = SubsonicRequestExecutor(urlBuilder)
        var handlerCalled = false

        val result = executor.executeStreaming("stream.view") { handlerCalled = true }

        assertTrue(result is SubsonicResult.Failure)
        assertEquals(SubsonicErrorCode.GENERIC_ERROR, result.error.code)
        assertTrue(!handlerCalled)
    }
}
