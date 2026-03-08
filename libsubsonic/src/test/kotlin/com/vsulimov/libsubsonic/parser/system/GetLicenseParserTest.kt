package com.vsulimov.libsubsonic.parser.system

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.json.JSONObject

class GetLicenseParserTest {

    @Test
    fun `parse extracts license fields correctly`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "license": {
                    "valid": true,
                    "email": "foo@bar.com",
                    "licenseExpires": "2019-09-03T14:46:43"
                }
            }
        """.trimIndent()

        val response = GetLicenseParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.13.0", response.apiVersion)
        assertTrue(response.license.valid)
        assertEquals("foo@bar.com", response.license.email)
        assertEquals("2019-09-03T14:46:43", response.license.licenseExpires)
    }

    @Test
    fun `parse handles missing optional license fields`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0",
                "license": {
                    "valid": false
                }
            }
        """.trimIndent()

        val response = GetLicenseParser.parse(JSONObject(jsonString))

        assertFalse(response.license.valid)
        assertNull(response.license.email)
        assertNull(response.license.licenseExpires)
    }

    @Test
    fun `parse returns invalid license when license object is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.13.0"
            }
        """.trimIndent()

        val response = GetLicenseParser.parse(JSONObject(jsonString))

        assertFalse(response.license.valid)
        assertNull(response.license.email)
        assertNull(response.license.licenseExpires)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetLicenseParser.parse(
            TestFixtures.navidromeResponseJson("license", """{"valid": true}""")
        )

        TestFixtures.assertNavidromeMetadata(response)
        assertTrue(response.license.valid)
    }
}
