package com.vsulimov.libsubsonic.parser.internetradio

import com.vsulimov.libsubsonic.data.TestFixtures
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.json.JSONObject

class GetInternetRadioStationsParserTest {

    @Test
    fun `parse returns stations from internetRadioStations container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "internetRadioStations": {
                    "internetRadioStation": [
                        {
                            "id": "0",
                            "name": "NRK P1",
                            "streamUrl": "http://lyd.nrk.no/nrk_radio_p1_mp3_m",
                            "homePageUrl": "http://www.nrk.no/p1"
                        },
                        {
                            "id": "1",
                            "name": "NRK P2",
                            "streamUrl": "http://lyd.nrk.no/nrk_radio_p2_mp3_m",
                            "homePageUrl": "http://p3.no"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetInternetRadioStationsParser.parse(JSONObject(jsonString))

        assertEquals("ok", response.status)
        assertEquals("1.9.0", response.apiVersion)
        assertEquals(2, response.stations.size)

        val first = response.stations[0]
        assertEquals("0", first.id)
        assertEquals("NRK P1", first.name)
        assertEquals("http://lyd.nrk.no/nrk_radio_p1_mp3_m", first.streamUrl)
        assertEquals("http://www.nrk.no/p1", first.homePageUrl)

        val second = response.stations[1]
        assertEquals("1", second.id)
        assertEquals("NRK P2", second.name)
        assertEquals("http://lyd.nrk.no/nrk_radio_p2_mp3_m", second.streamUrl)
        assertEquals("http://p3.no", second.homePageUrl)
    }

    @Test
    fun `parse handles missing optional homePageUrl`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "internetRadioStations": {
                    "internetRadioStation": [
                        {
                            "id": "0",
                            "name": "NRK P1",
                            "streamUrl": "http://lyd.nrk.no/nrk_radio_p1_mp3_m"
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = GetInternetRadioStationsParser.parse(JSONObject(jsonString))

        assertNull(response.stations[0].homePageUrl)
    }

    @Test
    fun `parse handles single station object instead of array`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "internetRadioStations": {
                    "internetRadioStation": {
                        "id": "0",
                        "name": "NRK P1",
                        "streamUrl": "http://lyd.nrk.no/nrk_radio_p1_mp3_m"
                    }
                }
            }
        """.trimIndent()

        val response = GetInternetRadioStationsParser.parse(JSONObject(jsonString))

        assertEquals(1, response.stations.size)
        assertEquals("0", response.stations[0].id)
    }

    @Test
    fun `parse returns empty list when container is absent`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0"
            }
        """.trimIndent()

        val response = GetInternetRadioStationsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.stations.size)
    }

    @Test
    fun `parse returns empty list when station array is absent inside container`() {
        val jsonString = """
            {
                "status": "ok",
                "version": "1.9.0",
                "internetRadioStations": {}
            }
        """.trimIndent()

        val response = GetInternetRadioStationsParser.parse(JSONObject(jsonString))

        assertEquals(0, response.stations.size)
    }

    @Test
    fun `parse captures server metadata fields`() {
        val response = GetInternetRadioStationsParser.parse(
            TestFixtures.navidromeResponseJson("internetRadioStations", "{}")
        )

        TestFixtures.assertNavidromeMetadata(response)
    }
}
