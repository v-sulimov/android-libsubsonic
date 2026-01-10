package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.ArtistEntry
import com.vsulimov.libsubsonic.data.response.browsing.Child
import com.vsulimov.libsubsonic.data.response.browsing.GenreEntry
import com.vsulimov.libsubsonic.data.response.browsing.ReplayGain
import com.vsulimov.libsubsonic.data.response.browsing.SongResponse
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getSong` response payload.
 */
internal object GetSongParser {

    /**
     * Parses the "subsonic-response" object into a [SongResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [SongResponse].
     */
    fun parse(json: JSONObject): SongResponse {
        val song = json.optJSONObject("song")?.let { parseSong(it) }
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
     * Parses a song or child object into a [Child] model.
     *
     * @param json The song or child JSONObject.
     * @return The parsed [Child] instance.
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
        val isrc = json.optJSONArray("isrc")?.let { array ->
            (0 until array.length()).map { i -> array.getString(i) }
        } ?: emptyList()
        val moods = json.optJSONArray("moods")?.let { array ->
            (0 until array.length()).map { i -> array.getString(i) }
        } ?: emptyList()
        val contributors = json.optJSONArray("contributors")?.let { array ->
            (0 until array.length()).map { i -> array.getString(i) }
        } ?: emptyList()
        val replayGain = json.optJSONObject("replayGain")?.let {
            ReplayGain(
                albumGain = it.optDouble("albumGain"),
                artistGain = it.optDouble("artistGain"),
                trackPeak = it.optDouble("trackPeak"),
                albumPeak = it.optDouble("albumPeak"),
                trackGain = it.optDouble("trackGain")
            )
        }

        return Child(
            id = json.optString("id"),
            parent = json.optString("parent").ifEmpty { null },
            isDir = json.optBoolean("isDir", false),
            title = json.optString("title"),
            album = json.optString("album").ifEmpty { null },
            albumId = json.optString("albumId").ifEmpty { null },
            artist = json.optString("artist").ifEmpty { null },
            artistId = json.optString("artistId").ifEmpty { null },
            track = if (json.has("track")) json.optInt("track") else null,
            year = if (json.has("year")) json.optInt("year") else null,
            genre = json.optString("genre").ifEmpty { null },
            coverArt = json.optString("coverArt").ifEmpty { null },
            duration = if (json.has("duration")) json.optInt("duration") else null,
            created = json.optString("created").ifEmpty { null },
            songCount = if (json.has("songCount")) json.optInt("songCount") else null,
            isVideo = json.optBoolean("isVideo", false),
            playCount = if (json.has("playCount")) json.optInt("playCount") else null,
            played = json.optString("played").ifEmpty { null },
            starred = json.optString("starred").ifEmpty { null },
            type = json.optString("type").ifEmpty { null },
            mediaType = json.optString("mediaType").ifEmpty { null },
            bitRate = if (json.has("bitRate")) json.optInt("bitRate") else null,
            bitDepth = if (json.has("bitDepth")) json.optInt("bitDepth") else null,
            samplingRate = if (json.has("samplingRate")) json.optInt("samplingRate") else null,
            channelCount = if (json.has("channelCount")) json.optInt("channelCount") else null,
            suffix = json.optString("suffix").ifEmpty { null },
            contentType = json.optString("contentType").ifEmpty { null },
            size = if (json.has("size")) json.optLong("size") else null,
            bpm = if (json.has("bpm")) json.optInt("bpm") else null,
            comment = json.optString("comment").ifEmpty { null },
            sortName = json.optString("sortName").ifEmpty { null },
            musicBrainzId = json.optString("musicBrainzId").ifEmpty { null },
            isrc = isrc,
            explicitStatus = json.optString("explicitStatus").ifEmpty { null },
            displayArtist = json.optString("displayArtist").ifEmpty { null },
            displayAlbumArtist = json.optString("displayAlbumArtist").ifEmpty { null },
            path = json.optString("path").ifEmpty { null },
            transcodedContentType = json.optString("transcodedContentType").ifEmpty { null },
            transcodedSuffix = json.optString("transcodedSuffix").ifEmpty { null },
            artists = artists,
            albumArtists = albumArtists,
            genres = genres,
            moods = moods,
            replayGain = replayGain,
            contributors = contributors,
            displayComposer = json.optString("displayComposer").ifEmpty { null }
        )
    }
}
