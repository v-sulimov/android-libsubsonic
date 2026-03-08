package com.vsulimov.libsubsonic.parser.sharing

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class DeleteShareParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.6.0"
            }
        """.trimIndent()

        val response = DeleteShareParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.6.0", response.apiVersion)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = DeleteShareParser.parse(
            TestFixtures.navidromeResponseJson("ignored", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
