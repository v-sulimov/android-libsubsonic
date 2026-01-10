package com.vsulimov.libsubsonic.data

import com.vsulimov.libsubsonic.data.response.SubsonicResponse
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.json.JSONObject

/**
 * Shared test fixtures providing reusable JSON builders and assertion helpers
 * for Subsonic API parser unit tests.
 *
 * Use [navidromeResponseJson] to build standard Navidrome-flavored JSON objects
 * without repeating the metadata headers in every test, and [assertNavidromeMetadata]
 * to verify all five server-metadata fields in a single call.
 */
object TestFixtures {

    /** Expected `status` value for a successful Navidrome response. */
    const val STATUS_OK = "ok"

    /** Subsonic REST API version reported by the test Navidrome server. */
    const val NAVIDROME_API_VERSION = "1.16.1"

    /** Server type string reported by Navidrome. */
    const val NAVIDROME_SERVER_TYPE = "navidrome"

    /** Software version reported by the test Navidrome server. */
    const val NAVIDROME_SERVER_VERSION = "0.59.0"

    /**
     * Builds a [JSONObject] containing the standard Navidrome server metadata fields
     * plus an optional container entry identified by [containerKey].
     *
     * This helper eliminates the repetitive JSON boilerplate in "captures server metadata"
     * tests. The resulting object is equivalent to the inner content of a real
     * `subsonic-response` envelope.
     *
     * Example usage:
     * ```kotlin
     * val response = GetAlbumListParser.parse(
     *     TestFixtures.navidromeResponseJson("albumList2", """{"album": []}""")
     * )
     * TestFixtures.assertNavidromeMetadata(response)
     * ```
     *
     * @param containerKey The JSON key for the domain-specific container field.
     * @param containerValue A JSON string for the container value (defaults to `{}`).
     * @return A [JSONObject] ready to pass directly to a parser.
     */
    fun navidromeResponseJson(containerKey: String, containerValue: String = "{}"): JSONObject =
        JSONObject(
            """
            {
                "status": "ok",
                "version": "1.16.1",
                "type": "navidrome",
                "serverVersion": "0.59.0",
                "openSubsonic": true,
                "$containerKey": $containerValue
            }
            """.trimIndent()
        )

    /**
     * Asserts that the given [response] contains the standard Navidrome server metadata.
     *
     * Verifies all five envelope fields:
     * - `status` equals `"ok"`
     * - `apiVersion` equals `"1.16.1"`
     * - `serverType` equals `"navidrome"`
     * - `serverVersion` equals `"0.59.0"`
     * - `isOpenSubsonic` is `true`
     *
     * @param response The [SubsonicResponse] to verify.
     */
    fun assertNavidromeMetadata(response: SubsonicResponse) {
        assertEquals(STATUS_OK, response.status)
        assertEquals(NAVIDROME_API_VERSION, response.apiVersion)
        assertEquals(NAVIDROME_SERVER_TYPE, response.serverType)
        assertEquals(NAVIDROME_SERVER_VERSION, response.serverVersion)
        assertTrue(response.isOpenSubsonic)
    }
}
