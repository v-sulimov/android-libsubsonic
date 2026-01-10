package com.vsulimov.libsubsonic.url

import com.vsulimov.libsubsonic.data.TestConstants.DEFAULT_CLIENT_NAME
import com.vsulimov.libsubsonic.data.TestConstants.PASSWORD
import com.vsulimov.libsubsonic.data.TestConstants.USERNAME
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SubsonicUrlBuilderTest {

    private lateinit var urlBuilder: SubsonicUrlBuilder

    @BeforeTest
    fun setup() {
        urlBuilder = SubsonicUrlBuilder(baseUrl = "http://test.subsonic.org", clientName = DEFAULT_CLIENT_NAME)
    }

    @Test
    fun `buildUrl fails when credentials are missing`() {
        val exception = assertFailsWith<IllegalStateException> {
            urlBuilder.buildUrl("ping")
        }
        assertEquals(expected = "Credentials not set. Call setCredentials() first.", actual = exception.message)
    }

    @Test
    fun `buildUrl generates a valid signed query string`() {
        urlBuilder.setCredentials(username = USERNAME, password = PASSWORD)
        val url = urlBuilder.buildUrl(endpoint = "ping")

        // Assert Path
        assertTrue(actual = url.contains(other = "/rest/ping?"))

        // Assert Credentials & Format
        assertTrue(url.contains(other = "u=$USERNAME"))
        assertTrue(url.contains(other = "c=$DEFAULT_CLIENT_NAME"))
        assertTrue(url.contains(other = "f=json"))

        // Assert Security (Salt and Token presence)
        assertTrue(url.contains(other = "&s="))
        assertTrue(url.contains(other = "&t="))
    }

    @Test
    fun `buildUrl corrects base URL trailing slash`() {
        val builder = SubsonicUrlBuilder(baseUrl = "http://test2.subsonic.org", clientName = DEFAULT_CLIENT_NAME)
        builder.setCredentials(username = USERNAME, password = PASSWORD)
        val url = builder.buildUrl(endpoint = "test.view")

        assertTrue(url.startsWith("http://test2.subsonic.org/rest/test.view"))
    }

    @Test
    fun `buildUrl correctly appends custom parameters`() {
        val builder = SubsonicUrlBuilder(baseUrl = "http://test.subsonic.org", clientName = DEFAULT_CLIENT_NAME)
        builder.setCredentials(username = USERNAME, password = PASSWORD)

        val params = mapOf("musicFolderId" to "1", "ifModifiedSince" to "123456789")
        val url = builder.buildUrl(endpoint = "getIndexes.view", params = params)

        assertTrue(actual = url.contains(other = "f=json"), message = "Should contain JSON format param")
        assertTrue(actual = url.contains(other = "musicFolderId=1"), message = "Should contain musicFolderId")
        assertTrue(actual = url.contains(other = "ifModifiedSince=123456789"), message = "Should contain timestamp")
        assertTrue(actual = url.contains(other = "u=$USERNAME"), message = "Should contain authentication params")
    }
}
