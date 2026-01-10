package com.vsulimov.libsubsonic.parser.browsing

import com.vsulimov.libsubsonic.data.response.browsing.ArtistEntry
import com.vsulimov.libsubsonic.data.response.browsing.Child
import com.vsulimov.libsubsonic.data.response.browsing.GenreEntry
import com.vsulimov.libsubsonic.data.response.browsing.ReplayGain
import com.vsulimov.libsubsonic.data.response.browsing.SongResponse
import org.json.JSONObject

/**
 * Internal parser responsible for extracting [SongResponse] from the JSON response.
 */
internal object GetSongParser {

    /**
     * Parses the "subsonic-response" object into a [SongResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return A [SongResponse] containing metadata and the requested song.
     */
    fun parse(json: JSONObject): SongResponse {
        val songObj = json.optJSONObject("song")
        val song = if (songObj != null) {
            parseSong(songObj)
        } else {
            Child(id = "", parent = "", isDir = false, title = "")
        }

        return SongResponse(
            status = json.optString("status", "ok"),
            apiVersion = json.optString("version", "Unknown"),
            serverType = json.optString("type").ifEmpty { null },
            serverVersion = json.optString("serverVersion").ifEmpty { null },
            isOpenSubsonic = json.optBoolean("openSubsonic", false),
            song = song
        )
    }

    /**
     * Internal helper to parse a song/child object.
     * This is also used by other parsers to ensure consistency.
     */
    fun parseSong(json: JSONObject): Child {
        val artists = mutableListOf<ArtistEntry>()
        json.optJSONArray("artists")?.let { array ->
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                artists.add(
                    ArtistEntry(
                        id = obj.optString("id"),
                        name = obj.optString("name")
                    )
                )
            }
        }

        val albumArtists = mutableListOf<ArtistEntry>()
        json.optJSONArray("albumArtists")?.let { array ->
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                albumArtists.add(
                    ArtistEntry(
                        id = obj.optString("id"),
                        name = obj.optString("name")
                    )
                )
            }
        }

        val genres = mutableListOf<GenreEntry>()
        json.optJSONArray("genres")?.let { array ->
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                genres.add(GenreEntry(name = obj.optString("name")))
            }
        }

        val isrc = mutableListOf<String>()
        json.optJSONArray("isrc")?.let {
            for (i in 0 until it.length()) {
                isrc.add(it.getString(i))
            }
        }

        val moods = mutableListOf<String>()
        json.optJSONArray("moods")?.let {
            for (i in 0 until it.length()) {
                moods.add(it.getString(i))
            }
        }

        val contributors = mutableListOf<String>()
        json.optJSONArray("contributors")?.let {
            for (i in 0 until it.length()) {
                contributors.add(it.getString(i))
            }
        }

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
            parent = json.optString("parent"),
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
