package com.vsulimov.libsubsonic.data.response.browsing

/**
 * Represents an item within a directory or an album (either a sub-directory or a media file).
 *
 * This model captures extensive metadata returned by browsing endpoints.
 *
 * @property id The unique identifier for the item.
 * @property parent The identifier of the parent directory, if available.
 * @property isDir True if this item is a directory, false if it is a media file.
 * @property title The display title or name of the item.
 * @property album The name of the album this item belongs to.
 * @property albumId The unique identifier for the album.
 * @property artist The name of the artist associated with this item.
 * @property artistId The unique identifier for the artist.
 * @property track The track number of the song in the album.
 * @property year The release year.
 * @property genre The primary musical genre.
 * @property coverArt The identifier for the cover art.
 * @property duration The duration in seconds.
 * @property created The ISO 8601 timestamp indicating when this item was added to the library.
 * @property songCount If this is a directory, the number of songs it contains.
 * @property isVideo True if this is a video file.
 * @property playCount The number of times this item has been played.
 * @property played The ISO 8601 timestamp indicating when this item was last played.
 * @property starred The ISO 8601 timestamp indicating when this item was starred by the user.
 * @property type The Subsonic item type (for example, "music").
 * @property mediaType The type of media (e.g., "album", "song").
 * @property bitRate The bit rate of the media file in kbps.
 * @property bitDepth The bit depth of the media file (e.g., 16, 24).
 * @property samplingRate The sampling rate of the media file in Hz (e.g., 44100).
 * @property channelCount The number of audio channels (e.g., 2 for stereo).
 * @property suffix The file extension (e.g., "mp3", "flac").
 * @property contentType The MIME type (e.g., "audio/mpeg").
 * @property size The file size in bytes.
 * @property bpm Beats per minute.
 * @property comment User comment associated with the file.
 * @property sortName The name used for sorting purposes.
 * @property musicBrainzId The MusicBrainz identifier for the recording.
 * @property isrc International Standard Recording Code.
 * @property explicitStatus The content advisory status (e.g., "explicit").
 * @property displayArtist The artist name formatted for display.
 * @property displayAlbumArtist The album artist name formatted for display.
 * @property path The file path on the server.
 * @property transcodedContentType The transcoded MIME type, if applicable.
 * @property transcodedSuffix The transcoded file extension, if applicable.
 * @property artists List of artists associated with this song (OpenSubsonic).
 * @property albumArtists List of album artists associated with this song (OpenSubsonic).
 * @property genres List of genres associated with this song (OpenSubsonic).
 * @property moods List of moods associated with this song (OpenSubsonic).
 * @property replayGain ReplayGain information (OpenSubsonic).
 * @property contributors List of contributors associated with this song (OpenSubsonic).
 * @property displayComposer The composer name formatted for display.
 */
data class Child(
    val id: String,
    val parent: String? = null,
    val isDir: Boolean,
    val title: String,
    val album: String? = null,
    val albumId: String? = null,
    val artist: String? = null,
    val artistId: String? = null,
    val track: Int? = null,
    val year: Int? = null,
    val genre: String? = null,
    val coverArt: String? = null,
    val duration: Int? = null,
    val created: String? = null,
    val songCount: Int? = null,
    val isVideo: Boolean = false,
    val playCount: Int? = null,
    val played: String? = null,
    val starred: String? = null,
    val type: String? = null,
    val mediaType: String? = null,
    val bitRate: Int? = null,
    val bitDepth: Int? = null,
    val samplingRate: Int? = null,
    val channelCount: Int? = null,
    val suffix: String? = null,
    val contentType: String? = null,
    val size: Long? = null,
    val bpm: Int? = null,
    val comment: String? = null,
    val sortName: String? = null,
    val musicBrainzId: String? = null,
    val isrc: List<String> = emptyList(),
    val explicitStatus: String? = null,
    val displayArtist: String? = null,
    val displayAlbumArtist: String? = null,
    val path: String? = null,
    val transcodedContentType: String? = null,
    val transcodedSuffix: String? = null,
    val artists: List<ArtistEntry> = emptyList(),
    val albumArtists: List<ArtistEntry> = emptyList(),
    val genres: List<GenreEntry> = emptyList(),
    val moods: List<String> = emptyList(),
    val replayGain: ReplayGain? = null,
    val contributors: List<String> = emptyList(),
    val displayComposer: String? = null
)
