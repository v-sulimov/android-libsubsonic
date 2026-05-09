package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.ArtistEntry
import com.vsulimov.libsubsonic.data.response.browsing.Child
import com.vsulimov.libsubsonic.data.response.browsing.GenreEntry
import com.vsulimov.libsubsonic.data.response.browsing.ReplayGain
import com.vsulimov.libsubsonic.data.response.browsing.SongResponse
import com.vsulimov.libsubsonic.parser.optDoubleOrNull
import com.vsulimov.libsubsonic.parser.optIntOrNull
import com.vsulimov.libsubsonic.parser.optLongOrNull
import com.vsulimov.libsubsonic.parser.optStringList
import com.vsulimov.libsubsonic.parser.optStringOrNull
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getSong` response payload and provides the shared [parseSong] helper used by
 * other parsers that embed a [Child] in their payloads (e.g. bookmarks, playlists, play queues).
 */
internal object GetSongParser {

    /**
     * Parses the `subsonic-response` object into a [SongResponse].
     *
     * If the `song` element is missing the response carries an empty placeholder [Child]; this
     * matches the historical behaviour of the library (which never raises a parse error for a
     * missing payload).
     *
     * @param json The unwrapped `subsonic-response` JSON object.
     * @return The parsed [SongResponse].
     */
    fun parse(json: JSONObject): SongResponse {
        val song = json.optJSONObject("song")?.let(::parseSong)
            ?: Child(id = "", isDir = false, title = "")

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return SongResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            song = song
        )
    }

    /**
     * Parses any `song`/`child` JSON object into a [Child] instance.
     *
     * Shared with other parsers (bookmarks, playlists, play queues, jukebox, podcasts, search,
     * lists, similar/top songs) that embed [Child] entries in their payloads.
     *
     * @param json The song or child JSON object.
     * @return The parsed [Child].
     */
    fun parseSong(json: JSONObject): Child {
        val artists = json.parseList("artists") { obj ->
            ArtistEntry(id = obj.optString("id"), name = obj.optString("name"))
        }
        val albumArtists = json.parseList("albumArtists") { obj ->
            ArtistEntry(id = obj.optString("id"), name = obj.optString("name"))
        }
        val genres = json.parseList("genres") { obj ->
            GenreEntry(name = obj.optString("name"))
        }
        val replayGain = json.optJSONObject("replayGain")?.let { obj ->
            ReplayGain(
                albumGain = obj.optDoubleOrNull("albumGain"),
                artistGain = obj.optDoubleOrNull("artistGain"),
                trackPeak = obj.optDoubleOrNull("trackPeak"),
                albumPeak = obj.optDoubleOrNull("albumPeak"),
                trackGain = obj.optDoubleOrNull("trackGain")
            )
        }

        return Child(
            id = json.optString("id"),
            parent = json.optStringOrNull("parent"),
            isDir = json.optBoolean("isDir", false),
            title = json.optString("title"),
            album = json.optStringOrNull("album"),
            albumId = json.optStringOrNull("albumId"),
            artist = json.optStringOrNull("artist"),
            artistId = json.optStringOrNull("artistId"),
            track = json.optIntOrNull("track"),
            year = json.optIntOrNull("year"),
            genre = json.optStringOrNull("genre"),
            coverArt = json.optStringOrNull("coverArt"),
            duration = json.optIntOrNull("duration"),
            created = json.optStringOrNull("created"),
            songCount = json.optIntOrNull("songCount"),
            isVideo = json.optBoolean("isVideo", false),
            playCount = json.optIntOrNull("playCount"),
            played = json.optStringOrNull("played"),
            starred = json.optStringOrNull("starred"),
            type = json.optStringOrNull("type"),
            mediaType = json.optStringOrNull("mediaType"),
            bitRate = json.optIntOrNull("bitRate"),
            bitDepth = json.optIntOrNull("bitDepth"),
            samplingRate = json.optIntOrNull("samplingRate"),
            channelCount = json.optIntOrNull("channelCount"),
            suffix = json.optStringOrNull("suffix"),
            contentType = json.optStringOrNull("contentType"),
            size = json.optLongOrNull("size"),
            bpm = json.optIntOrNull("bpm"),
            comment = json.optStringOrNull("comment"),
            sortName = json.optStringOrNull("sortName"),
            musicBrainzId = json.optStringOrNull("musicBrainzId"),
            isrc = json.optStringList("isrc"),
            explicitStatus = json.optStringOrNull("explicitStatus"),
            displayArtist = json.optStringOrNull("displayArtist"),
            displayAlbumArtist = json.optStringOrNull("displayAlbumArtist"),
            path = json.optStringOrNull("path"),
            transcodedContentType = json.optStringOrNull("transcodedContentType"),
            transcodedSuffix = json.optStringOrNull("transcodedSuffix"),
            artists = artists,
            albumArtists = albumArtists,
            genres = genres,
            moods = json.optStringList("moods"),
            replayGain = replayGain,
            contributors = json.optStringList("contributors"),
            displayComposer = json.optStringOrNull("displayComposer")
        )
    }
}
