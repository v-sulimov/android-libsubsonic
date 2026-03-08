package com.vsulimov.libsubsonic.parser.user

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class CreateUserParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.12.0"
            }
        """.trimIndent()

        val response = CreateUserParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.12.0", response.apiVersion)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = CreateUserParser.parse(
            TestFixtures.navidromeResponseJson("ignored", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
