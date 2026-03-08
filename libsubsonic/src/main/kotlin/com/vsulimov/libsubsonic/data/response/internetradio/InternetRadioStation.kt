package com.vsulimov.libsubsonic.data.response.internetradio

/**
 * Represents an internet radio station returned by the Subsonic server.
 *
 * @property id The unique identifier of the station.
 * @property name The display name of the station.
 * @property streamUrl The URL of the radio stream.
 * @property homePageUrl The URL of the station's home page.
 */
data class InternetRadioStation(
    val id: String,
    val name: String,
    val streamUrl: String,
    val homePageUrl: String? = null
)
