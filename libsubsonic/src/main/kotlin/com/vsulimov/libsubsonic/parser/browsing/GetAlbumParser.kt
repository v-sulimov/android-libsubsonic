package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.Album
import com.vsulimov.libsubsonic.data.response.browsing.AlbumResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getAlbum` response payload.
 */
internal object GetAlbumParser {

    /**
     * Parses the "subsonic-response" object into an [AlbumResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [AlbumResponse].
     */
    fun parse(json: JSONObject): AlbumResponse {
        val albumObj = json.optJSONObject("album")
        val album = if (albumObj != null) {
            val songs = albumObj.parseList("song") { GetSongParser.parseSong(it) }
            parseAlbum(albumObj).copy(songs = songs)
        } else {
            Album(id = "", name = "")
        }

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
     * Parses a single album JSON object into an [Album] without embedded songs.
     *
     * This function is shared by [GetArtistParser] and the lists parsers that receive
     * lightweight album entries (no song children). Songs can be appended via [Album.copy]
     * when needed.
     *
     * @param json The JSON object representing an album.
     * @return The parsed [Album] with an empty songs list.
     */
    internal fun parseAlbum(json: JSONObject) = Album(
        id = json.optString("id"),
        name = json.optString("name"),
        artist = json.optString("artist").ifEmpty { null },
        artistId = json.optString("artistId").ifEmpty { null },
        coverArt = json.optString("coverArt").ifEmpty { null },
        songCount = if (json.has("songCount")) json.optInt("songCount") else null,
        duration = if (json.has("duration")) json.optInt("duration") else null,
        playCount = if (json.has("playCount")) json.optInt("playCount") else null,
        created = json.optString("created").ifEmpty { null },
        year = if (json.has("year")) json.optInt("year") else null,
        genre = json.optString("genre").ifEmpty { null },
        played = json.optString("played").ifEmpty { null },
        starred = json.optString("starred").ifEmpty { null },
        userRating = if (json.has("userRating")) json.optInt("userRating") else null,
        musicBrainzId = json.optString("musicBrainzId").ifEmpty { null },
        isCompilation = json.optBoolean("isCompilation", false),
        sortName = json.optString("sortName").ifEmpty { null },
        displayArtist = json.optString("displayArtist").ifEmpty { null },
        explicitStatus = json.optString("explicitStatus").ifEmpty { null }
    )
}
