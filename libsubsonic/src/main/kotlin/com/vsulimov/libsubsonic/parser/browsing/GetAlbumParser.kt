package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Album
import com.vsulimov.libsubsonic.data.response.browsing.AlbumResponse
import com.vsulimov.libsubsonic.parser.optIntOrNull
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getAlbum` response payload and provides the shared [parseAlbum] helper used by
 * other parsers that embed [Album] entries in their payloads (artist details, list endpoints).
 */
internal object GetAlbumParser {

    /**
     * Parses the `subsonic-response` object into an [AlbumResponse].
     *
     * If the `album` element is missing the response carries an empty placeholder [Album].
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [AlbumResponse].
     */
    fun parse(json: JSONObject): AlbumResponse {
        val album = json.optJSONObject("album")?.let { obj ->
            val songs = obj.parseList("song", GetSongParser::parseSong)
            parseAlbum(obj).copy(songs = songs)
        } ?: Album(id = "", name = "")

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return AlbumResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            album = album
        )
    }

    /**
     * Parses a single `album` JSON object into an [Album] with an empty `songs` list.
     *
     * Shared by [GetArtistParser] and the lists parsers that receive lightweight album entries
     * (no song children). Callers can append songs via [Album.copy] when needed.
     *
     * @param json The JSON object representing an album.
     * @return The parsed [Album] with no embedded songs.
     */
    internal fun parseAlbum(json: JSONObject) = Album(
        id = json.optString("id"),
        name = json.optString("name"),
        artist = json.optStringOrNull("artist"),
        artistId = json.optStringOrNull("artistId"),
        coverArt = json.optStringOrNull("coverArt"),
        songCount = json.optIntOrNull("songCount"),
        duration = json.optIntOrNull("duration"),
        playCount = json.optIntOrNull("playCount"),
        created = json.optStringOrNull("created"),
        year = json.optIntOrNull("year"),
        genre = json.optStringOrNull("genre"),
        played = json.optStringOrNull("played"),
        starred = json.optStringOrNull("starred"),
        userRating = json.optIntOrNull("userRating"),
        musicBrainzId = json.optStringOrNull("musicBrainzId"),
        isCompilation = json.optBoolean("isCompilation", false),
        sortName = json.optStringOrNull("sortName"),
        displayArtist = json.optStringOrNull("displayArtist"),
        explicitStatus = json.optStringOrNull("explicitStatus")
    )
}
