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

        assertTrue(actual = url.contains(other = "/rest/ping?"))
        assertTrue(url.contains(other = "u=$USERNAME"))
        assertTrue(url.contains(other = "c=$DEFAULT_CLIENT_NAME"))
        assertTrue(url.contains(other = "f=json"))
        assertTrue(url.contains(other = "&s="))
        assertTrue(url.contains(other = "&t="))
    }

    @Test
    fun `buildUrl appends rest path to base URL without trailing slash`() {
        val builder = SubsonicUrlBuilder(baseUrl = "http://test2.subsonic.org", clientName = DEFAULT_CLIENT_NAME)
        builder.setCredentials(username = USERNAME, password = PASSWORD)
        val url = builder.buildUrl(endpoint = "test.view")

        assertTrue(url.startsWith("http://test2.subsonic.org/rest/test.view"))
    }

    @Test
    fun `buildUrl handles base URL with trailing slash without double slash`() {
        val builder = SubsonicUrlBuilder(baseUrl = "http://test.subsonic.org/", clientName = DEFAULT_CLIENT_NAME)
        builder.setCredentials(username = USERNAME, password = PASSWORD)
        val url = builder.buildUrl(endpoint = "ping")

        assertTrue(url.startsWith("http://test.subsonic.org/rest/ping"))
        assertTrue(!url.contains("//rest/"))
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

    @Test
    fun `buildUrl repeats multi-value parameter key once per value`() {
        val builder = SubsonicUrlBuilder(baseUrl = "http://test.subsonic.org", clientName = DEFAULT_CLIENT_NAME)
        builder.setCredentials(username = USERNAME, password = PASSWORD)

        val multiValueParams = mapOf("songId" to listOf("1", "2", "3"))
        val url = builder.buildUrl(endpoint = "createPlaylist.view", multiValueParams = multiValueParams)

        val occurrences = url.split("songId=").size - 1
        assertEquals(expected = 3, actual = occurrences, message = "songId should appear exactly three times")
        assertTrue(actual = url.contains(other = "songId=1"), message = "Should contain songId=1")
        assertTrue(actual = url.contains(other = "songId=2"), message = "Should contain songId=2")
        assertTrue(actual = url.contains(other = "songId=3"), message = "Should contain songId=3")
        assertTrue(actual = url.contains(other = "u=$USERNAME"), message = "Should contain authentication params")
    }

    @Test
    fun `buildUrl URL-encodes special characters in parameter values`() {
        val builder = SubsonicUrlBuilder(baseUrl = "http://test.subsonic.org", clientName = DEFAULT_CLIENT_NAME)
        builder.setCredentials(username = USERNAME, password = PASSWORD)

        val params = mapOf("query" to "rock & roll", "artist" to "AC/DC")
        val url = builder.buildUrl(endpoint = "search3.view", params = params)

        assertTrue(url.contains("query=rock+%26+roll") || url.contains("query=rock%20%26%20roll"))
        assertTrue(url.contains("artist=AC%2FDC"))
    }

    @Test
    fun `buildUrl handles base URL with path components`() {
        val builder =
            SubsonicUrlBuilder(baseUrl = "http://test.subsonic.org/subsonic", clientName = DEFAULT_CLIENT_NAME)
        builder.setCredentials(username = USERNAME, password = PASSWORD)
        val url = builder.buildUrl(endpoint = "ping")

        assertTrue(url.startsWith("http://test.subsonic.org/subsonic/rest/ping"))
    }

    @Test
    fun `buildUrl handles empty params and multiValueParams`() {
        urlBuilder.setCredentials(username = USERNAME, password = PASSWORD)
        val url = urlBuilder.buildUrl(endpoint = "ping", params = emptyMap(), multiValueParams = emptyMap())

        assertTrue(url.contains("f=json"))
        assertTrue(url.contains("u=$USERNAME"))
    }

    @Test
    fun `setCredentials replaces previous credentials`() {
        urlBuilder.setCredentials(username = "user1", password = "pass1")
        val url1 = urlBuilder.buildUrl(endpoint = "ping")
        assertTrue(url1.contains("u=user1"))

        urlBuilder.setCredentials(username = "user2", password = "pass2")
        val url2 = urlBuilder.buildUrl(endpoint = "ping")
        assertTrue(url2.contains("u=user2"))
    }
}
