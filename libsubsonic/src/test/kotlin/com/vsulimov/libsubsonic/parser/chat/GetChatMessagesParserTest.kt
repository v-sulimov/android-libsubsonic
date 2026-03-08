package com.vsulimov.libsubsonic.parser.chat

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import org.json.JSONObject

class GetChatMessagesParserTest {

    @Test
    fun `parse extracts status and version from successful response`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.2.0"
            }
        """.trimIndent()

        val response = GetChatMessagesParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.2.0", response.apiVersion)
    }

    @Test
    fun `parse returns empty list when chatMessages is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.2.0"
            }
        """.trimIndent()

        val response = GetChatMessagesParser.parse(JSONObject(jsonString))

        assertEquals(emptyList(), response.messages)
    }

    @Test
    fun `parse extracts chat messages`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.2.0",
                "chatMessages": {
                    "chatMessage": [
                        {"username": "sindre", "time": 1269771845310, "message": "Sindre was here"},
                        {"username": "ben", "time": 1269771842504, "message": "Ben too"}
                    ]
                }
            }
        """.trimIndent()

        val response = GetChatMessagesParser.parse(JSONObject(jsonString))

        assertEquals(2, response.messages.size)
        assertEquals("sindre", response.messages[0].username)
        assertEquals(1269771845310L, response.messages[0].time)
        assertEquals("Sindre was here", response.messages[0].message)
        assertEquals("ben", response.messages[1].username)
        assertEquals(1269771842504L, response.messages[1].time)
        assertEquals("Ben too", response.messages[1].message)
    }

    @Test
    fun `parse handles single chatMessage object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.2.0",
                "chatMessages": {
                    "chatMessage": {"username": "sindre", "time": 1269771845310, "message": "Hello"}
                }
            }
        """.trimIndent()

        val response = GetChatMessagesParser.parse(JSONObject(jsonString))

        assertEquals(1, response.messages.size)
        assertEquals("sindre", response.messages[0].username)
        assertEquals("Hello", response.messages[0].message)
    }

    @Test
    fun `parse returns empty list when chatMessages container is empty`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.2.0",
                "chatMessages": {}
            }
        """.trimIndent()

        val response = GetChatMessagesParser.parse(JSONObject(jsonString))

        assertEquals(emptyList(), response.messages)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetChatMessagesParser.parse(
            TestFixtures.navidromeResponseJson("chatMessages", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
