package com.vsulimov.libsubsonic

import com.vsulimov.libsubsonic.auth.encodePassword
import com.vsulimov.libsubsonic.data.Constants.DEFAULT_CLIENT_NAME
import com.vsulimov.libsubsonic.data.response.annotation.Rating
import com.vsulimov.libsubsonic.data.response.annotation.ScrobbleResponse
import com.vsulimov.libsubsonic.data.response.annotation.SetRatingResponse
import com.vsulimov.libsubsonic.data.response.annotation.StarResponse
import com.vsulimov.libsubsonic.data.response.annotation.UnstarResponse
import com.vsulimov.libsubsonic.data.response.bookmark.BookmarksResponse
import com.vsulimov.libsubsonic.data.response.bookmark.CreateBookmarkResponse
import com.vsulimov.libsubsonic.data.response.bookmark.DeleteBookmarkResponse
import com.vsulimov.libsubsonic.data.response.browsing.AlbumInfoResponse
import com.vsulimov.libsubsonic.data.response.browsing.AlbumResponse
import com.vsulimov.libsubsonic.data.response.browsing.ArtistInfoResponse
import com.vsulimov.libsubsonic.data.response.browsing.ArtistResponse
import com.vsulimov.libsubsonic.data.response.browsing.ArtistsResponse
import com.vsulimov.libsubsonic.data.response.browsing.GenresResponse
import com.vsulimov.libsubsonic.data.response.browsing.IndexesResponse
import com.vsulimov.libsubsonic.data.response.browsing.MusicDirectoryResponse
import com.vsulimov.libsubsonic.data.response.browsing.MusicFoldersResponse
import com.vsulimov.libsubsonic.data.response.browsing.SimilarSongsResponse
import com.vsulimov.libsubsonic.data.response.browsing.SongResponse
import com.vsulimov.libsubsonic.data.response.browsing.TopSongsResponse
import com.vsulimov.libsubsonic.data.response.chat.AddChatMessageResponse
import com.vsulimov.libsubsonic.data.response.chat.ChatMessagesResponse
import com.vsulimov.libsubsonic.data.response.internetradio.CreateInternetRadioStationResponse
import com.vsulimov.libsubsonic.data.response.internetradio.DeleteInternetRadioStationResponse
import com.vsulimov.libsubsonic.data.response.internetradio.InternetRadioStationsResponse
import com.vsulimov.libsubsonic.data.response.internetradio.UpdateInternetRadioStationResponse
import com.vsulimov.libsubsonic.data.response.jukebox.JukeboxAction
import com.vsulimov.libsubsonic.data.response.jukebox.JukeboxResponse
import com.vsulimov.libsubsonic.data.response.lists.AlbumListResponse
import com.vsulimov.libsubsonic.data.response.lists.AlbumListType
import com.vsulimov.libsubsonic.data.response.lists.NowPlayingResponse
import com.vsulimov.libsubsonic.data.response.lists.RandomSongsResponse
import com.vsulimov.libsubsonic.data.response.lists.SongsByGenreResponse
import com.vsulimov.libsubsonic.data.response.lists.StarredResponse
import com.vsulimov.libsubsonic.data.response.lyrics.LyricsResponse
import com.vsulimov.libsubsonic.data.response.playlists.DeletePlaylistResponse
import com.vsulimov.libsubsonic.data.response.playlists.PlaylistResponse
import com.vsulimov.libsubsonic.data.response.playlists.PlaylistsResponse
import com.vsulimov.libsubsonic.data.response.playlists.UpdatePlaylistResponse
import com.vsulimov.libsubsonic.data.response.playqueue.PlayQueueResponse
import com.vsulimov.libsubsonic.data.response.playqueue.SavePlayQueueResponse
import com.vsulimov.libsubsonic.data.response.podcast.CreatePodcastChannelResponse
import com.vsulimov.libsubsonic.data.response.podcast.DeletePodcastChannelResponse
import com.vsulimov.libsubsonic.data.response.podcast.DeletePodcastEpisodeResponse
import com.vsulimov.libsubsonic.data.response.podcast.DownloadPodcastEpisodeResponse
import com.vsulimov.libsubsonic.data.response.podcast.NewestPodcastsResponse
import com.vsulimov.libsubsonic.data.response.podcast.PodcastsResponse
import com.vsulimov.libsubsonic.data.response.podcast.RefreshPodcastsResponse
import com.vsulimov.libsubsonic.data.response.scan.ScanStatusResponse
import com.vsulimov.libsubsonic.data.response.search.SearchResponse
import com.vsulimov.libsubsonic.data.response.sharing.DeleteShareResponse
import com.vsulimov.libsubsonic.data.response.sharing.SharesResponse
import com.vsulimov.libsubsonic.data.response.sharing.UpdateShareResponse
import com.vsulimov.libsubsonic.data.response.system.LicenseResponse
import com.vsulimov.libsubsonic.data.response.system.PingResponse
import com.vsulimov.libsubsonic.data.response.user.ChangePasswordResponse
import com.vsulimov.libsubsonic.data.response.user.CreateUserResponse
import com.vsulimov.libsubsonic.data.response.user.DeleteUserResponse
import com.vsulimov.libsubsonic.data.response.user.MaxBitRate
import com.vsulimov.libsubsonic.data.response.user.UpdateUserResponse
import com.vsulimov.libsubsonic.data.response.user.UserResponse
import com.vsulimov.libsubsonic.data.response.user.UsersResponse
import com.vsulimov.libsubsonic.data.response.video.VideoInfoResponse
import com.vsulimov.libsubsonic.data.response.video.VideosResponse
import com.vsulimov.libsubsonic.data.result.SubsonicResult
import com.vsulimov.libsubsonic.executor.SubsonicRequestExecutor
import com.vsulimov.libsubsonic.http.HttpClient
import com.vsulimov.libsubsonic.http.HttpClient.Companion.DEFAULT_CONNECT_TIMEOUT_MS
import com.vsulimov.libsubsonic.http.HttpClient.Companion.DEFAULT_READ_TIMEOUT_MS
import com.vsulimov.libsubsonic.parser.annotation.ScrobbleParser
import com.vsulimov.libsubsonic.parser.annotation.SetRatingParser
import com.vsulimov.libsubsonic.parser.annotation.StarParser
import com.vsulimov.libsubsonic.parser.annotation.UnstarParser
import com.vsulimov.libsubsonic.parser.bookmark.CreateBookmarkParser
import com.vsulimov.libsubsonic.parser.bookmark.DeleteBookmarkParser
import com.vsulimov.libsubsonic.parser.bookmark.GetBookmarksParser
import com.vsulimov.libsubsonic.parser.browsing.GetAlbumInfoParser
import com.vsulimov.libsubsonic.parser.browsing.GetAlbumParser
import com.vsulimov.libsubsonic.parser.browsing.GetArtistInfoParser
import com.vsulimov.libsubsonic.parser.browsing.GetArtistParser
import com.vsulimov.libsubsonic.parser.browsing.GetArtistsParser
import com.vsulimov.libsubsonic.parser.browsing.GetGenresParser
import com.vsulimov.libsubsonic.parser.browsing.GetIndexesParser
import com.vsulimov.libsubsonic.parser.browsing.GetMusicDirectoryParser
import com.vsulimov.libsubsonic.parser.browsing.GetMusicFoldersParser
import com.vsulimov.libsubsonic.parser.browsing.GetSimilarSongsParser
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.browsing.GetTopSongsParser
import com.vsulimov.libsubsonic.parser.chat.AddChatMessageParser
import com.vsulimov.libsubsonic.parser.chat.GetChatMessagesParser
import com.vsulimov.libsubsonic.parser.internetradio.CreateInternetRadioStationParser
import com.vsulimov.libsubsonic.parser.internetradio.DeleteInternetRadioStationParser
import com.vsulimov.libsubsonic.parser.internetradio.GetInternetRadioStationsParser
import com.vsulimov.libsubsonic.parser.internetradio.UpdateInternetRadioStationParser
import com.vsulimov.libsubsonic.parser.jukebox.JukeboxControlParser
import com.vsulimov.libsubsonic.parser.lists.GetAlbumListParser
import com.vsulimov.libsubsonic.parser.lists.GetNowPlayingParser
import com.vsulimov.libsubsonic.parser.lists.GetRandomSongsParser
import com.vsulimov.libsubsonic.parser.lists.GetSongsByGenreParser
import com.vsulimov.libsubsonic.parser.lists.GetStarredParser
import com.vsulimov.libsubsonic.parser.lyrics.GetLyricsParser
import com.vsulimov.libsubsonic.parser.playlists.CreatePlaylistParser
import com.vsulimov.libsubsonic.parser.playlists.DeletePlaylistParser
import com.vsulimov.libsubsonic.parser.playlists.GetPlaylistParser
import com.vsulimov.libsubsonic.parser.playlists.GetPlaylistsParser
import com.vsulimov.libsubsonic.parser.playlists.UpdatePlaylistParser
import com.vsulimov.libsubsonic.parser.playqueue.GetPlayQueueParser
import com.vsulimov.libsubsonic.parser.playqueue.SavePlayQueueParser
import com.vsulimov.libsubsonic.parser.podcast.CreatePodcastChannelParser
import com.vsulimov.libsubsonic.parser.podcast.DeletePodcastChannelParser
import com.vsulimov.libsubsonic.parser.podcast.DeletePodcastEpisodeParser
import com.vsulimov.libsubsonic.parser.podcast.DownloadPodcastEpisodeParser
import com.vsulimov.libsubsonic.parser.podcast.GetNewestPodcastsParser
import com.vsulimov.libsubsonic.parser.podcast.GetPodcastsParser
import com.vsulimov.libsubsonic.parser.podcast.RefreshPodcastsParser
import com.vsulimov.libsubsonic.parser.scan.GetScanStatusParser
import com.vsulimov.libsubsonic.parser.scan.StartScanParser
import com.vsulimov.libsubsonic.parser.search.SearchParser
import com.vsulimov.libsubsonic.parser.sharing.DeleteShareParser
import com.vsulimov.libsubsonic.parser.sharing.GetSharesParser
import com.vsulimov.libsubsonic.parser.sharing.UpdateShareParser
import com.vsulimov.libsubsonic.parser.system.GetLicenseParser
import com.vsulimov.libsubsonic.parser.system.PingParser
import com.vsulimov.libsubsonic.parser.user.ChangePasswordParser
import com.vsulimov.libsubsonic.parser.user.CreateUserParser
import com.vsulimov.libsubsonic.parser.user.DeleteUserParser
import com.vsulimov.libsubsonic.parser.user.GetUserParser
import com.vsulimov.libsubsonic.parser.user.GetUsersParser
import com.vsulimov.libsubsonic.parser.user.UpdateUserParser
import com.vsulimov.libsubsonic.parser.video.GetVideoInfoParser
import com.vsulimov.libsubsonic.parser.video.GetVideosParser
import com.vsulimov.libsubsonic.url.SubsonicUrlBuilder
import java.io.InputStream

/**
 * Entry point for the Subsonic REST API client.
 *
 * This client provides a coroutine-based interface to a Subsonic server while
 * encapsulating authentication, URL signing, and JSON parsing.
 *
 * @param baseUrl The full URL to the Subsonic server (for example, "http://myserver.com/").
 * @param clientName A unique identifier for this client application.
 * @param connectTimeoutMs Maximum time in milliseconds to establish a connection. Defaults to 15 000.
 * @param readTimeoutMs Maximum time in milliseconds to wait for data during a read. Defaults to 30 000.
 */
class SubsonicClient(
    baseUrl: String,
    clientName: String = DEFAULT_CLIENT_NAME,
    connectTimeoutMs: Int = DEFAULT_CONNECT_TIMEOUT_MS,
    readTimeoutMs: Int = DEFAULT_READ_TIMEOUT_MS
) {

    /** Builds signed URLs for every API call using the configured base URL and credentials. */
    private val urlBuilder = SubsonicUrlBuilder(baseUrl, clientName)

    /** Executes API requests by delegating network I/O and response parsing to the appropriate components. */
    private val executor = SubsonicRequestExecutor(urlBuilder, HttpClient(connectTimeoutMs, readTimeoutMs))

    /**
     * Sets the user credentials for future API calls.
     *
     * This method stores the credentials in memory to generate salted MD5 tokens
     * for subsequent requests.
     *
     * @param username The Subsonic username.
     * @param password The clear-text password.
     */
    fun setCredentials(username: String, password: String) {
        urlBuilder.setCredentials(username, password)
    }

    /**
     * Pings the server to verify connectivity and retrieve server metadata.
     *
     * @return [SubsonicResult.Success] containing server details ([PingResponse]).
     * @return [SubsonicResult.Failure] if the server is unreachable or credentials are invalid.
     */
    suspend fun ping(): SubsonicResult<PingResponse> = executor.execute("ping.view") { jsonObject ->
        PingParser.parse(jsonObject)
    }

    /**
     * Returns the current license information for the Subsonic server.
     *
     * @return [SubsonicResult.Success] containing [LicenseResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getLicense(): SubsonicResult<LicenseResponse> = executor.execute("getLicense.view") { jsonObject ->
        GetLicenseParser.parse(jsonObject)
    }

    /**
     * Returns all configured top-level music folders.
     *
     * @return [SubsonicResult.Success] containing [MusicFoldersResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getMusicFolders(): SubsonicResult<MusicFoldersResponse> =
        executor.execute("getMusicFolders.view") { jsonObject ->
            GetMusicFoldersParser.parse(jsonObject)
        }

    /**
     * Returns an indexed list of artists.
     *
     * @param musicFolderId Optional ID of the music folder to browse.
     * @param ifModifiedSince Optional timestamp; if provided, only returns data if changed since then.
     * @return [SubsonicResult.Success] containing [IndexesResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getIndexes(
        musicFolderId: String? = null,
        ifModifiedSince: Long? = null
    ): SubsonicResult<IndexesResponse> {
        val params = mutableMapOf<String, String>()
        musicFolderId?.let { params["musicFolderId"] = it }
        ifModifiedSince?.let { params["ifModifiedSince"] = it.toString() }

        return executor.execute("getIndexes.view", params) { jsonObject ->
            GetIndexesParser.parse(jsonObject)
        }
    }

    /**
     * Returns the contents of a specific music directory.
     *
     * This method is used to browse the library folder-by-folder. The returned
     * [MusicDirectoryResponse] contains the directory structure and its children.
     *
     * @param id The unique identifier of the directory to browse.
     * @return [SubsonicResult.Success] containing the [MusicDirectoryResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getMusicDirectory(id: String): SubsonicResult<MusicDirectoryResponse> {
        val params = mapOf("id" to id)
        return executor.execute("getMusicDirectory.view", params) { jsonObject ->
            GetMusicDirectoryParser.parse(jsonObject)
        }
    }

    /**
     * Retrieves all genres categorized by the server.
     *
     * This provides a high-level view of the library's metadata, including
     * statistics on the number of albums and songs per genre.
     *
     * @return [SubsonicResult.Success] containing [GenresResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getGenres(): SubsonicResult<GenresResponse> = executor.execute("getGenres.view") { jsonObject ->
        GetGenresParser.parse(jsonObject)
    }

    /**
     * Returns a list of all artists based on ID3 tags, grouped alphabetically.
     *
     * This is the preferred method for ID3-based browsing, as opposed to
     * [getIndexes] which is more suitable for file-system browsing.
     *
     * @param musicFolderId Optional. If specified, only return artists in this folder.
     * @return [SubsonicResult.Success] containing [ArtistsResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getArtists(musicFolderId: String? = null): SubsonicResult<ArtistsResponse> {
        val params = mutableMapOf<String, String>()
        musicFolderId?.let { params["musicFolderId"] = it }

        return executor.execute("getArtists.view", params) { jsonObject ->
            GetArtistsParser.parse(jsonObject)
        }
    }

    /**
     * Returns details for an artist, including a list of albums.
     *
     * This is an ID3-based endpoint.
     *
     * @param id The unique identifier for the artist.
     * @return [SubsonicResult.Success] containing [ArtistResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getArtist(id: String): SubsonicResult<ArtistResponse> {
        val params = mapOf("id" to id)
        return executor.execute("getArtist.view", params) { jsonObject ->
            GetArtistParser.parse(jsonObject)
        }
    }

    /**
     * Returns details for an album, including its list of songs.
     *
     * This is an ID3-based endpoint.
     *
     * @param id The unique identifier for the album.
     * @return [SubsonicResult.Success] containing [AlbumResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getAlbum(id: String): SubsonicResult<AlbumResponse> {
        val params = mapOf("id" to id)
        return executor.execute("getAlbum.view", params) { jsonObject ->
            GetAlbumParser.parse(jsonObject)
        }
    }

    /**
     * Returns details for a song.
     *
     * This is an ID3-based endpoint.
     *
     * @param id The unique identifier for the song.
     * @return [SubsonicResult.Success] containing [SongResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getSong(id: String): SubsonicResult<SongResponse> {
        val params = mapOf("id" to id)
        return executor.execute("getSong.view", params) { jsonObject ->
            GetSongParser.parse(jsonObject)
        }
    }

    /**
     * Returns all video files available on the server.
     *
     * @return [SubsonicResult.Success] containing [VideosResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getVideos(): SubsonicResult<VideosResponse> = executor.execute("getVideos.view") { jsonObject ->
        GetVideosParser.parse(jsonObject)
    }

    /**
     * Returns details for a video, including available audio tracks, captions, and conversion profiles.
     *
     * @param id The unique identifier of the video.
     * @return [SubsonicResult.Success] containing [VideoInfoResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getVideoInfo(id: String): SubsonicResult<VideoInfoResponse> {
        val params = mapOf("id" to id)
        return executor.execute("getVideoInfo.view", params) { jsonObject ->
            GetVideoInfoParser.parse(jsonObject)
        }
    }

    /**
     * Returns extended artist metadata, such as biographies and similar artists.
     *
     * This is an ID3-based endpoint.
     *
     * @param id The unique identifier for the artist.
     * @param count Optional. Maximum number of similar artists to return. Defaults to 20 server-side.
     * @param includeNotPresent Optional. Whether to return artists not present in the media library.
     * @return [SubsonicResult.Success] containing [ArtistInfoResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getArtistInfo(
        id: String,
        count: Int? = null,
        includeNotPresent: Boolean? = null
    ): SubsonicResult<ArtistInfoResponse> {
        val params = mutableMapOf("id" to id)
        count?.let { params["count"] = it.toString() }
        includeNotPresent?.let { params["includeNotPresent"] = it.toString() }
        return executor.execute("getArtistInfo2.view", params) { jsonObject ->
            GetArtistInfoParser.parse(jsonObject)
        }
    }

    /**
     * Returns extended album metadata, such as notes and related metadata.
     *
     * This is an ID3-based endpoint.
     *
     * @param id The unique identifier for the album.
     * @return [SubsonicResult.Success] containing [AlbumInfoResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getAlbumInfo(id: String): SubsonicResult<AlbumInfoResponse> {
        val params = mapOf("id" to id)
        return executor.execute("getAlbumInfo2.view", params) { jsonObject ->
            GetAlbumInfoParser.parse(jsonObject)
        }
    }

    /**
     * Returns a list of songs similar to the given song.
     *
     * This is an ID3-based endpoint.
     *
     * @param id The unique identifier for the song.
     * @param count Optional. The maximum number of similar songs to return. Defaults to 50 server-side.
     * @return [SubsonicResult.Success] containing [SimilarSongsResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getSimilarSongs(id: String, count: Int? = null): SubsonicResult<SimilarSongsResponse> {
        val params = mutableMapOf("id" to id)
        count?.let { params["count"] = it.toString() }
        return executor.execute("getSimilarSongs2.view", params) { jsonObject ->
            GetSimilarSongsParser.parse(jsonObject)
        }
    }

    /**
     * Returns the top songs for a given artist.
     *
     * @param artistName The name of the artist.
     * @param count Optional. The maximum number of songs to return. Defaults to 50 server-side.
     * @return [SubsonicResult.Success] containing [TopSongsResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getTopSongs(artistName: String, count: Int? = null): SubsonicResult<TopSongsResponse> {
        val params = mutableMapOf("artist" to artistName)
        count?.let { params["count"] = it.toString() }
        return executor.execute("getTopSongs.view", params) { jsonObject ->
            GetTopSongsParser.parse(jsonObject)
        }
    }

    /**
     * Returns a list of albums using the ID3-based endpoint.
     *
     * @param type The list type. See [AlbumListType] for all supported values.
     * @param size Optional. The maximum number of albums to return. Defaults to 10 server-side, max 500.
     * @param offset Optional. The list offset. Use for pagination.
     * @param fromYear Optional. The first year in the range when [type] is [AlbumListType.BY_YEAR].
     * @param toYear Optional. The last year in the range when [type] is [AlbumListType.BY_YEAR].
     * @param genre Optional. The genre to filter by when [type] is [AlbumListType.BY_GENRE].
     * @param musicFolderId Optional. Only return albums from the music folder with the given id.
     * @return [SubsonicResult.Success] containing [AlbumListResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getAlbumList(
        type: AlbumListType,
        size: Int? = null,
        offset: Int? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        genre: String? = null,
        musicFolderId: String? = null
    ): SubsonicResult<AlbumListResponse> {
        val params = mutableMapOf("type" to type.value)
        size?.let { params["size"] = it.toString() }
        offset?.let { params["offset"] = it.toString() }
        fromYear?.let { params["fromYear"] = it.toString() }
        toYear?.let { params["toYear"] = it.toString() }
        genre?.let { params["genre"] = it }
        musicFolderId?.let { params["musicFolderId"] = it }
        return executor.execute("getAlbumList2.view", params) { jsonObject ->
            GetAlbumListParser.parse(jsonObject)
        }
    }

    /**
     * Returns a randomly selected set of songs.
     *
     * @param size Optional. The maximum number of songs to return. Defaults to 10 server-side, max 500.
     * @param genre Optional. Only return songs from this genre.
     * @param fromYear Optional. Only return songs published after (or in) this year.
     * @param toYear Optional. Only return songs published before (or in) this year.
     * @param musicFolderId Optional. Only return songs from the music folder with the given id.
     * @return [SubsonicResult.Success] containing [RandomSongsResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getRandomSongs(
        size: Int? = null,
        genre: String? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        musicFolderId: String? = null
    ): SubsonicResult<RandomSongsResponse> {
        val params = mutableMapOf<String, String>()
        size?.let { params["size"] = it.toString() }
        genre?.let { params["genre"] = it }
        fromYear?.let { params["fromYear"] = it.toString() }
        toYear?.let { params["toYear"] = it.toString() }
        musicFolderId?.let { params["musicFolderId"] = it }
        return executor.execute("getRandomSongs.view", params) { jsonObject ->
            GetRandomSongsParser.parse(jsonObject)
        }
    }

    /**
     * Returns songs in a given genre.
     *
     * @param genre The genre to return songs for.
     * @param count Optional. The maximum number of songs to return. Defaults to 10 server-side, max 500.
     * @param offset Optional. The list offset. Use for pagination.
     * @param musicFolderId Optional. Only return songs from the music folder with the given id.
     * @return [SubsonicResult.Success] containing [SongsByGenreResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getSongsByGenre(
        genre: String,
        count: Int? = null,
        offset: Int? = null,
        musicFolderId: String? = null
    ): SubsonicResult<SongsByGenreResponse> {
        val params = mutableMapOf("genre" to genre)
        count?.let { params["count"] = it.toString() }
        offset?.let { params["offset"] = it.toString() }
        musicFolderId?.let { params["musicFolderId"] = it }
        return executor.execute("getSongsByGenre.view", params) { jsonObject ->
            GetSongsByGenreParser.parse(jsonObject)
        }
    }

    /**
     * Returns what is currently being played by all users.
     *
     * @return [SubsonicResult.Success] containing [NowPlayingResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getNowPlaying(): SubsonicResult<NowPlayingResponse> =
        executor.execute("getNowPlaying.view", emptyMap()) { jsonObject ->
            GetNowPlayingParser.parse(jsonObject)
        }

    /**
     * Returns starred songs, albums and artists using ID3 tags.
     *
     * @param musicFolderId Optional. If specified, only return starred items from this music folder.
     * @return [SubsonicResult.Success] containing [StarredResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getStarred(musicFolderId: String? = null): SubsonicResult<StarredResponse> {
        val params = mutableMapOf<String, String>()
        musicFolderId?.let { params["musicFolderId"] = it }
        return executor.execute("getStarred2.view", params) { jsonObject ->
            GetStarredParser.parse(jsonObject)
        }
    }

    /**
     * Searches for artists, albums, and songs using ID3 tags.
     *
     * @param query The search query string.
     * @param artistCount Optional. Maximum number of artists to return. Defaults to 20 server-side.
     * @param artistOffset Optional. Search result offset for artists. Use for pagination.
     * @param albumCount Optional. Maximum number of albums to return. Defaults to 20 server-side.
     * @param albumOffset Optional. Search result offset for albums. Use for pagination.
     * @param songCount Optional. Maximum number of songs to return. Defaults to 20 server-side.
     * @param songOffset Optional. Search result offset for songs. Use for pagination.
     * @param musicFolderId Optional. Only return results from the music folder with the given id.
     * @return [SubsonicResult.Success] containing [SearchResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun search(
        query: String,
        artistCount: Int? = null,
        artistOffset: Int? = null,
        albumCount: Int? = null,
        albumOffset: Int? = null,
        songCount: Int? = null,
        songOffset: Int? = null,
        musicFolderId: String? = null
    ): SubsonicResult<SearchResponse> {
        val params = mutableMapOf("query" to query)
        artistCount?.let { params["artistCount"] = it.toString() }
        artistOffset?.let { params["artistOffset"] = it.toString() }
        albumCount?.let { params["albumCount"] = it.toString() }
        albumOffset?.let { params["albumOffset"] = it.toString() }
        songCount?.let { params["songCount"] = it.toString() }
        songOffset?.let { params["songOffset"] = it.toString() }
        musicFolderId?.let { params["musicFolderId"] = it }
        return executor.execute("search3.view", params) { jsonObject ->
            SearchParser.parse(jsonObject)
        }
    }

    /**
     * Returns all playlists a user is allowed to play.
     *
     * @param username Optional. If specified, return playlists for this user rather than for the
     * authenticated user. The authenticated user must have admin role if this parameter is used.
     * @return [SubsonicResult.Success] containing [PlaylistsResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getPlaylists(username: String? = null): SubsonicResult<PlaylistsResponse> {
        val params = mutableMapOf<String, String>()
        username?.let { params["username"] = it }
        return executor.execute("getPlaylists.view", params) { jsonObject ->
            GetPlaylistsParser.parse(jsonObject)
        }
    }

    /**
     * Returns a playlist with its song entries.
     *
     * @param id The unique identifier of the playlist.
     * @return [SubsonicResult.Success] containing [PlaylistResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getPlaylist(id: String): SubsonicResult<PlaylistResponse> {
        val params = mapOf("id" to id)
        return executor.execute("getPlaylist.view", params) { jsonObject ->
            GetPlaylistParser.parse(jsonObject)
        }
    }

    /**
     * Creates a new playlist or replaces the contents of an existing playlist.
     *
     * Since API version 1.14.0 the server returns the newly created or updated playlist.
     * Either [playlistId] (when updating) or [name] (when creating) must be specified.
     *
     * @param playlistId The ID of the playlist to update. Required when updating an existing playlist.
     * @param name The human-readable name for the playlist. Required when creating a new playlist.
     * @param songIds Optional. The IDs of songs to include in the playlist. Each ID is sent as a
     *   separate `songId` query parameter.
     * @return [SubsonicResult.Success] containing the created or updated [PlaylistResponse].
     * @return [SubsonicResult.Failure] if the request fails or required parameters are absent.
     */
    suspend fun createPlaylist(
        playlistId: String? = null,
        name: String? = null,
        songIds: List<String> = emptyList()
    ): SubsonicResult<PlaylistResponse> {
        val params = mutableMapOf<String, String>()
        playlistId?.let { params["playlistId"] = it }
        name?.let { params["name"] = it }
        val multiValueParams = if (songIds.isNotEmpty()) {
            mapOf("songId" to songIds)
        } else {
            emptyMap()
        }
        return executor.execute("createPlaylist.view", params, multiValueParams) { jsonObject ->
            CreatePlaylistParser.parse(jsonObject)
        }
    }

    /**
     * Updates an existing playlist.
     *
     * Only the parameters that are provided will be changed; omitted optional parameters
     * leave the corresponding playlist fields unchanged.
     *
     * @param playlistId The ID of the playlist to update. Required.
     * @param name Optional. A new human-readable name for the playlist.
     * @param comment Optional. A new comment or description for the playlist.
     * @param public Optional. Whether the playlist should be visible to all users.
     * @param songIdsToAdd Optional. IDs of songs to append to the playlist. Each ID is sent
     *   as a separate `songIdToAdd` query parameter.
     * @param songIndexesToRemove Optional. Zero-based indexes of songs to remove from the
     *   playlist. Each index is sent as a separate `songIndexToRemove` query parameter.
     * @return [SubsonicResult.Success] containing an [UpdatePlaylistResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [playlistId] is invalid.
     */
    suspend fun updatePlaylist(
        playlistId: String,
        name: String? = null,
        comment: String? = null,
        public: Boolean? = null,
        songIdsToAdd: List<String> = emptyList(),
        songIndexesToRemove: List<Int> = emptyList()
    ): SubsonicResult<UpdatePlaylistResponse> {
        val params = mutableMapOf("playlistId" to playlistId)
        name?.let { params["name"] = it }
        comment?.let { params["comment"] = it }
        public?.let { params["public"] = it.toString() }
        val multiValueParams = mutableMapOf<String, List<String>>()
        if (songIdsToAdd.isNotEmpty()) {
            multiValueParams["songIdToAdd"] = songIdsToAdd
        }
        if (songIndexesToRemove.isNotEmpty()) {
            multiValueParams["songIndexToRemove"] = songIndexesToRemove.map { it.toString() }
        }
        return executor.execute("updatePlaylist.view", params, multiValueParams) { jsonObject ->
            UpdatePlaylistParser.parse(jsonObject)
        }
    }

    /**
     * Deletes an existing playlist.
     *
     * @param id The unique identifier of the playlist to delete.
     * @return [SubsonicResult.Success] containing a [DeletePlaylistResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun deletePlaylist(id: String): SubsonicResult<DeletePlaylistResponse> {
        val params = mapOf("id" to id)
        return executor.execute("deletePlaylist.view", params) { jsonObject ->
            DeletePlaylistParser.parse(jsonObject)
        }
    }

    /**
     * Streams a media file from the Subsonic server.
     *
     * The [responseHandler] lambda receives the raw audio or video data as an [InputStream].
     * The caller is responsible for reading and closing the stream within the lambda.
     *
     * @param id The unique identifier of the file to stream.
     * @param maxBitRate If set, limits the stream bitrate to this value in kilobits per second.
     * @param format The preferred target format (e.g. "mp3", "flac").
     * @param timeOffset Only applicable to video. Specifies where to start streaming, in seconds.
     * @param size Only applicable to video. The requested video size (e.g. "640x480").
     * @param estimateContentLength Only applicable to video. Whether the server should estimate the content length.
     * @param converted Navidrome extension. Only applicable to video.
     * @param responseHandler A suspend lambda that consumes the [InputStream] response body.
     * @return [SubsonicResult.Success] with [Unit] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun stream(
        id: String,
        maxBitRate: Int? = null,
        format: String? = null,
        timeOffset: Int? = null,
        size: String? = null,
        estimateContentLength: Boolean? = null,
        converted: Boolean? = null,
        responseHandler: suspend (InputStream) -> Unit
    ): SubsonicResult<Unit> {
        val params = mutableMapOf("id" to id)
        maxBitRate?.let { params["maxBitRate"] = it.toString() }
        format?.let { params["format"] = it }
        timeOffset?.let { params["timeOffset"] = it.toString() }
        size?.let { params["size"] = it }
        estimateContentLength?.let { params["estimateContentLength"] = it.toString() }
        converted?.let { params["converted"] = it.toString() }
        return executor.executeStreaming("stream.view", params, responseHandler = responseHandler)
    }

    /**
     * Downloads a media file from the Subsonic server without transcoding.
     *
     * Unlike [stream], this endpoint always returns the original file without any server-side
     * transcoding. The [responseHandler] lambda receives the raw file data as an [InputStream].
     * The caller is responsible for reading and closing the stream within the lambda.
     *
     * @param id The unique identifier of the file to download.
     * @param responseHandler A suspend lambda that consumes the [InputStream] response body.
     * @return [SubsonicResult.Success] with [Unit] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun download(
        id: String,
        responseHandler: suspend (InputStream) -> Unit
    ): SubsonicResult<Unit> {
        val params = mapOf("id" to id)
        return executor.executeStreaming("download.view", params, responseHandler = responseHandler)
    }

    /**
     * Creates an HLS (HTTP Live Streaming) playlist for streaming video or audio.
     *
     * Returns an M3U8 playlist consumed via [responseHandler]. Providing multiple [bitRates]
     * produces a variant playlist suitable for adaptive bitrate streaming.
     *
     * @param id The unique identifier of the media file to stream.
     * @param bitRates Optional. Bitrate values in kilobits per second to include in the playlist.
     *   Multiple values produce a variant (adaptive) playlist; a single value caps the bitrate.
     * @param audioTrack Optional. The ID of the audio track to use. Only applicable to video.
     * @param responseHandler A suspend lambda that consumes the [InputStream] response body.
     * @return [SubsonicResult.Success] with [Unit] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun hls(
        id: String,
        bitRates: List<Int> = emptyList(),
        audioTrack: String? = null,
        responseHandler: suspend (InputStream) -> Unit
    ): SubsonicResult<Unit> {
        val params = mutableMapOf("id" to id)
        audioTrack?.let { params["audioTrack"] = it }
        val multiValueParams = if (bitRates.isNotEmpty()) {
            mapOf("bitRate" to bitRates.map { it.toString() })
        } else {
            emptyMap()
        }
        return executor.executeStreaming("hls.view", params, multiValueParams, responseHandler)
    }

    /**
     * Returns captions (subtitles) for a video file.
     *
     * The [responseHandler] lambda receives the captions data as an [InputStream].
     * The caller is responsible for reading and closing the stream within the lambda.
     *
     * @param id The unique identifier of the video.
     * @param format Optional. The preferred captions format ("srt" or "vtt").
     * @param responseHandler A suspend lambda that consumes the [InputStream] response body.
     * @return [SubsonicResult.Success] with [Unit] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun getCaptions(
        id: String,
        format: String? = null,
        responseHandler: suspend (InputStream) -> Unit
    ): SubsonicResult<Unit> {
        val params = mutableMapOf("id" to id)
        format?.let { params["format"] = it }
        return executor.executeStreaming("getCaptions.view", params, responseHandler = responseHandler)
    }

    /**
     * Returns the cover art image for a song, album, or artist.
     *
     * The [responseHandler] lambda receives the image data as an [InputStream].
     * The caller is responsible for reading and closing the stream within the lambda.
     *
     * @param id The unique identifier of the song, album, or artist.
     * @param size Optional. The requested image size (width and height) in pixels.
     * @param responseHandler A suspend lambda that consumes the [InputStream] response body.
     * @return [SubsonicResult.Success] with [Unit] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun getCoverArt(
        id: String,
        size: Int? = null,
        responseHandler: suspend (InputStream) -> Unit
    ): SubsonicResult<Unit> {
        val params = mutableMapOf("id" to id)
        size?.let { params["size"] = it.toString() }
        return executor.executeStreaming("getCoverArt.view", params, responseHandler = responseHandler)
    }

    /**
     * Returns lyrics for a song, optionally searched by artist and title.
     *
     * @param artist Optional. The artist name to search for.
     * @param title Optional. The song title to search for.
     * @return [SubsonicResult.Success] containing [LyricsResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getLyrics(
        artist: String? = null,
        title: String? = null
    ): SubsonicResult<LyricsResponse> {
        val params = mutableMapOf<String, String>()
        artist?.let { params["artist"] = it }
        title?.let { params["title"] = it }
        return executor.execute("getLyrics.view", params) { jsonObject ->
            GetLyricsParser.parse(jsonObject)
        }
    }

    /**
     * Returns the avatar image for a user.
     *
     * The [responseHandler] lambda receives the image data as an [InputStream].
     * The caller is responsible for reading and closing the stream within the lambda.
     *
     * @param username The username for which to retrieve the avatar.
     * @param responseHandler A suspend lambda that consumes the [InputStream] response body.
     * @return [SubsonicResult.Success] with [Unit] on success.
     * @return [SubsonicResult.Failure] if the request fails or [username] is invalid.
     */
    suspend fun getAvatar(
        username: String,
        responseHandler: suspend (InputStream) -> Unit
    ): SubsonicResult<Unit> {
        val params = mapOf("username" to username)
        return executor.executeStreaming("getAvatar.view", params, responseHandler = responseHandler)
    }

    /**
     * Attaches a star to songs, albums, or artists.
     *
     * @param ids Optional. IDs of songs or folders to star.
     * @param albumIds Optional. IDs of albums to star (ID3-based).
     * @param artistIds Optional. IDs of artists to star (ID3-based).
     * @return [SubsonicResult.Success] containing [StarResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun star(
        ids: List<String> = emptyList(),
        albumIds: List<String> = emptyList(),
        artistIds: List<String> = emptyList()
    ): SubsonicResult<StarResponse> {
        val multiValueParams = mutableMapOf<String, List<String>>()
        if (ids.isNotEmpty()) multiValueParams["id"] = ids
        if (albumIds.isNotEmpty()) multiValueParams["albumId"] = albumIds
        if (artistIds.isNotEmpty()) multiValueParams["artistId"] = artistIds
        return executor.execute("star.view", multiValueParams = multiValueParams) { jsonObject ->
            StarParser.parse(jsonObject)
        }
    }

    /**
     * Removes the star from songs, albums, or artists.
     *
     * @param ids Optional. IDs of songs or folders to unstar.
     * @param albumIds Optional. IDs of albums to unstar (ID3-based).
     * @param artistIds Optional. IDs of artists to unstar (ID3-based).
     * @return [SubsonicResult.Success] containing [UnstarResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun unstar(
        ids: List<String> = emptyList(),
        albumIds: List<String> = emptyList(),
        artistIds: List<String> = emptyList()
    ): SubsonicResult<UnstarResponse> {
        val multiValueParams = mutableMapOf<String, List<String>>()
        if (ids.isNotEmpty()) multiValueParams["id"] = ids
        if (albumIds.isNotEmpty()) multiValueParams["albumId"] = albumIds
        if (artistIds.isNotEmpty()) multiValueParams["artistId"] = artistIds
        return executor.execute("unstar.view", multiValueParams = multiValueParams) { jsonObject ->
            UnstarParser.parse(jsonObject)
        }
    }

    /**
     * Sets the rating for a music file.
     *
     * @param id The unique identifier of the file to rate.
     * @param rating The rating to set. Use [Rating.REMOVE] to remove an existing rating.
     * @return [SubsonicResult.Success] containing [SetRatingResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun setRating(id: String, rating: Rating): SubsonicResult<SetRatingResponse> {
        val params = mapOf("id" to id, "rating" to rating.value.toString())
        return executor.execute("setRating.view", params) { jsonObject ->
            SetRatingParser.parse(jsonObject)
        }
    }

    /**
     * Registers playback of one or more media files.
     *
     * Each entry in [times] corresponds to the [ids] entry at the same index.
     *
     * @param ids The unique identifiers of the files to scrobble.
     * @param times Optional. The playback timestamps in milliseconds since 1 Jan 1970, one per id.
     * @param submission Optional. Whether this is a submission (true) or a "now playing" notification (false).
     * @return [SubsonicResult.Success] containing [ScrobbleResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun scrobble(
        ids: List<String>,
        times: List<Long> = emptyList(),
        submission: Boolean? = null
    ): SubsonicResult<ScrobbleResponse> {
        val params = mutableMapOf<String, String>()
        submission?.let { params["submission"] = it.toString() }
        val multiValueParams = mutableMapOf("id" to ids)
        if (times.isNotEmpty()) multiValueParams["time"] = times.map { it.toString() }
        return executor.execute("scrobble.view", params, multiValueParams) { jsonObject ->
            ScrobbleParser.parse(jsonObject)
        }
    }

    /**
     * Returns all shares created by the current user.
     *
     * @return [SubsonicResult.Success] containing [SharesResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getShares(): SubsonicResult<SharesResponse> =
        executor.execute("getShares.view") { jsonObject ->
            GetSharesParser.parse(jsonObject)
        }

    /**
     * Creates a new share for one or more media files.
     *
     * @param ids The unique identifiers of the files to include in the share.
     * @param description Optional. A user-defined description of the share.
     * @param expires Optional. The expiry time of the share in milliseconds since 1 Jan 1970.
     * @return [SubsonicResult.Success] containing [SharesResponse] with the created share.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun createShare(
        ids: List<String>,
        description: String? = null,
        expires: Long? = null
    ): SubsonicResult<SharesResponse> {
        val params = mutableMapOf<String, String>()
        description?.let { params["description"] = it }
        expires?.let { params["expires"] = it.toString() }
        val multiValueParams = mapOf("id" to ids)
        return executor.execute("createShare.view", params, multiValueParams) { jsonObject ->
            GetSharesParser.parse(jsonObject)
        }
    }

    /**
     * Updates an existing share.
     *
     * @param id The unique identifier of the share to update.
     * @param description Optional. A new description for the share.
     * @param expires Optional. The new expiry time in milliseconds since 1 Jan 1970.
     * @return [SubsonicResult.Success] containing [UpdateShareResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun updateShare(
        id: String,
        description: String? = null,
        expires: Long? = null
    ): SubsonicResult<UpdateShareResponse> {
        val params = mutableMapOf("id" to id)
        description?.let { params["description"] = it }
        expires?.let { params["expires"] = it.toString() }
        return executor.execute("updateShare.view", params) { jsonObject ->
            UpdateShareParser.parse(jsonObject)
        }
    }

    /**
     * Deletes an existing share.
     *
     * @param id The unique identifier of the share to delete.
     * @return [SubsonicResult.Success] containing [DeleteShareResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun deleteShare(id: String): SubsonicResult<DeleteShareResponse> {
        val params = mapOf("id" to id)
        return executor.execute("deleteShare.view", params) { jsonObject ->
            DeleteShareParser.parse(jsonObject)
        }
    }

    /**
     * Returns all podcast channels subscribed to by the server, optionally including episodes.
     *
     * @param includeEpisodes Optional. Whether to include episodes for each channel. Defaults to true server-side.
     * @param id Optional. If specified, return only the channel with this identifier.
     * @return [SubsonicResult.Success] containing [PodcastsResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getPodcasts(
        includeEpisodes: Boolean? = null,
        id: String? = null
    ): SubsonicResult<PodcastsResponse> {
        val params = mutableMapOf<String, String>()
        includeEpisodes?.let { params["includeEpisodes"] = it.toString() }
        id?.let { params["id"] = it }
        return executor.execute("getPodcasts.view", params) { jsonObject ->
            GetPodcastsParser.parse(jsonObject)
        }
    }

    /**
     * Returns the most recently published podcast episodes.
     *
     * @param count Optional. The maximum number of episodes to return. Defaults to 20 server-side.
     * @return [SubsonicResult.Success] containing [NewestPodcastsResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getNewestPodcasts(count: Int? = null): SubsonicResult<NewestPodcastsResponse> {
        val params = mutableMapOf<String, String>()
        count?.let { params["count"] = it.toString() }
        return executor.execute("getNewestPodcasts.view", params) { jsonObject ->
            GetNewestPodcastsParser.parse(jsonObject)
        }
    }

    /**
     * Requests the server to check for new podcast episodes.
     *
     * @return [SubsonicResult.Success] containing [RefreshPodcastsResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun refreshPodcasts(): SubsonicResult<RefreshPodcastsResponse> =
        executor.execute("refreshPodcasts.view") { jsonObject ->
            RefreshPodcastsParser.parse(jsonObject)
        }

    /**
     * Adds a new podcast channel.
     *
     * @param url The URL of the podcast channel's RSS feed.
     * @return [SubsonicResult.Success] containing [CreatePodcastChannelResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun createPodcastChannel(url: String): SubsonicResult<CreatePodcastChannelResponse> {
        val params = mapOf("url" to url)
        return executor.execute("createPodcastChannel.view", params) { jsonObject ->
            CreatePodcastChannelParser.parse(jsonObject)
        }
    }

    /**
     * Deletes a podcast channel.
     *
     * @param id The unique identifier of the podcast channel to delete.
     * @return [SubsonicResult.Success] containing [DeletePodcastChannelResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun deletePodcastChannel(id: String): SubsonicResult<DeletePodcastChannelResponse> {
        val params = mapOf("id" to id)
        return executor.execute("deletePodcastChannel.view", params) { jsonObject ->
            DeletePodcastChannelParser.parse(jsonObject)
        }
    }

    /**
     * Deletes a podcast episode.
     *
     * @param id The unique identifier of the podcast episode to delete.
     * @return [SubsonicResult.Success] containing [DeletePodcastEpisodeResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun deletePodcastEpisode(id: String): SubsonicResult<DeletePodcastEpisodeResponse> {
        val params = mapOf("id" to id)
        return executor.execute("deletePodcastEpisode.view", params) { jsonObject ->
            DeletePodcastEpisodeParser.parse(jsonObject)
        }
    }

    /**
     * Requests the server to start downloading a given podcast episode.
     *
     * @param id The unique identifier of the podcast episode to download.
     * @return [SubsonicResult.Success] containing [DownloadPodcastEpisodeResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun downloadPodcastEpisode(id: String): SubsonicResult<DownloadPodcastEpisodeResponse> {
        val params = mapOf("id" to id)
        return executor.execute("downloadPodcastEpisode.view", params) { jsonObject ->
            DownloadPodcastEpisodeParser.parse(jsonObject)
        }
    }

    /**
     * Controls the jukebox player on the server.
     *
     * Returns a [JukeboxResponse] containing playback status. When [action] is [JukeboxAction.GET],
     * the response also includes the current playlist entries.
     *
     * @param action The jukebox operation to perform.
     * @param index Optional. Zero-based playlist index. Used by [JukeboxAction.SKIP] and [JukeboxAction.REMOVE].
     * @param offset Optional. Offset in seconds to start playback from. Used by [JukeboxAction.SKIP].
     * @param ids Optional. Song IDs to add or set. Used by [JukeboxAction.ADD] and [JukeboxAction.SET].
     * @param gain Optional. Playback gain between 0.0 and 1.0. Used by [JukeboxAction.SET_GAIN].
     * @return [SubsonicResult.Success] containing [JukeboxResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun jukeboxControl(
        action: JukeboxAction,
        index: Int? = null,
        offset: Int? = null,
        ids: List<String> = emptyList(),
        gain: Double? = null
    ): SubsonicResult<JukeboxResponse> {
        val params = mutableMapOf("action" to action.value)
        index?.let { params["index"] = it.toString() }
        offset?.let { params["offset"] = it.toString() }
        gain?.let { params["gain"] = it.toString() }
        val multiValueParams = if (ids.isNotEmpty()) mapOf("id" to ids) else emptyMap()
        return executor.execute("jukeboxControl.view", params, multiValueParams) { jsonObject ->
            JukeboxControlParser.parse(jsonObject)
        }
    }

    /**
     * Returns all internet radio stations.
     *
     * @return [SubsonicResult.Success] containing [InternetRadioStationsResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getInternetRadioStations(): SubsonicResult<InternetRadioStationsResponse> =
        executor.execute("getInternetRadioStations.view") { jsonObject ->
            GetInternetRadioStationsParser.parse(jsonObject)
        }

    /**
     * Creates a new internet radio station.
     *
     * @param streamUrl The URL of the radio stream.
     * @param name The display name of the station.
     * @param homePageUrl Optional. The URL of the station's home page.
     * @return [SubsonicResult.Success] containing [CreateInternetRadioStationResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun createInternetRadioStation(
        streamUrl: String,
        name: String,
        homePageUrl: String? = null
    ): SubsonicResult<CreateInternetRadioStationResponse> {
        val params = mutableMapOf("streamUrl" to streamUrl, "name" to name)
        homePageUrl?.let { params["homepageUrl"] = it }
        return executor.execute("createInternetRadioStation.view", params) { jsonObject ->
            CreateInternetRadioStationParser.parse(jsonObject)
        }
    }

    /**
     * Updates an existing internet radio station.
     *
     * @param id The unique identifier of the station to update.
     * @param streamUrl The new URL of the radio stream.
     * @param name The new display name of the station.
     * @param homePageUrl Optional. The new URL of the station's home page.
     * @return [SubsonicResult.Success] containing [UpdateInternetRadioStationResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun updateInternetRadioStation(
        id: String,
        streamUrl: String,
        name: String,
        homePageUrl: String? = null
    ): SubsonicResult<UpdateInternetRadioStationResponse> {
        val params = mutableMapOf("id" to id, "streamUrl" to streamUrl, "name" to name)
        homePageUrl?.let { params["homepageUrl"] = it }
        return executor.execute("updateInternetRadioStation.view", params) { jsonObject ->
            UpdateInternetRadioStationParser.parse(jsonObject)
        }
    }

    /**
     * Deletes an existing internet radio station.
     *
     * @param id The unique identifier of the station to delete.
     * @return [SubsonicResult.Success] containing [DeleteInternetRadioStationResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or [id] is invalid.
     */
    suspend fun deleteInternetRadioStation(id: String): SubsonicResult<DeleteInternetRadioStationResponse> {
        val params = mapOf("id" to id)
        return executor.execute("deleteInternetRadioStation.view", params) { jsonObject ->
            DeleteInternetRadioStationParser.parse(jsonObject)
        }
    }

    /**
     * Returns the current visible (non-expired) chat messages.
     *
     * @param since Optional. Only return messages newer than this timestamp (milliseconds since epoch).
     * @return [SubsonicResult.Success] containing [ChatMessagesResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getChatMessages(since: Long? = null): SubsonicResult<ChatMessagesResponse> {
        val params = mutableMapOf<String, String>()
        since?.let { params["since"] = it.toString() }
        return executor.execute("getChatMessages.view", params) { jsonObject ->
            GetChatMessagesParser.parse(jsonObject)
        }
    }

    /**
     * Adds a message to the chat log.
     *
     * @param message The chat message to add.
     * @return [SubsonicResult.Success] containing [AddChatMessageResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun addChatMessage(message: String): SubsonicResult<AddChatMessageResponse> {
        val params = mapOf("message" to message)
        return executor.execute("addChatMessage.view", params) { jsonObject ->
            AddChatMessageParser.parse(jsonObject)
        }
    }

    /**
     * Returns details for a given user.
     *
     * @param username The name of the user to retrieve.
     * @return [SubsonicResult.Success] containing [UserResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or the user does not exist.
     */
    suspend fun getUser(username: String): SubsonicResult<UserResponse> {
        val params = mapOf("username" to username)
        return executor.execute("getUser.view", params) { jsonObject ->
            GetUserParser.parse(jsonObject)
        }
    }

    /**
     * Returns all users, including their roles.
     *
     * @return [SubsonicResult.Success] containing [UsersResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getUsers(): SubsonicResult<UsersResponse> {
        return executor.execute("getUsers.view") { jsonObject ->
            GetUsersParser.parse(jsonObject)
        }
    }

    /**
     * Creates a new user on the server.
     *
     * @param username The name of the new user.
     * @param password The password of the new user.
     * @param email The email address of the new user.
     * @param ldapAuthenticated Whether the user is authenticated in LDAP.
     * @param adminRole Whether the user is administrator.
     * @param settingsRole Whether the user is allowed to change personal settings and password.
     * @param streamRole Whether the user is allowed to play files.
     * @param jukeboxRole Whether the user is allowed to play files in jukebox mode.
     * @param downloadRole Whether the user is allowed to download files.
     * @param uploadRole Whether the user is allowed to upload files.
     * @param playlistRole Whether the user is allowed to create and delete playlists.
     * @param coverArtRole Whether the user is allowed to change cover art and tags.
     * @param commentRole Whether the user is allowed to create and edit comments and ratings.
     * @param podcastRole Whether the user is allowed to administrate Podcasts.
     * @param shareRole Whether the user is allowed to share files with anyone.
     * @param videoConversionRole Whether the user is allowed to start video conversions.
     * @param musicFolderIds IDs of the music folders the user is allowed access to.
     * @return [SubsonicResult.Success] containing [CreateUserResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun createUser(
        username: String,
        password: String,
        email: String,
        ldapAuthenticated: Boolean? = null,
        adminRole: Boolean? = null,
        settingsRole: Boolean? = null,
        streamRole: Boolean? = null,
        jukeboxRole: Boolean? = null,
        downloadRole: Boolean? = null,
        uploadRole: Boolean? = null,
        playlistRole: Boolean? = null,
        coverArtRole: Boolean? = null,
        commentRole: Boolean? = null,
        podcastRole: Boolean? = null,
        shareRole: Boolean? = null,
        videoConversionRole: Boolean? = null,
        musicFolderIds: List<String> = emptyList()
    ): SubsonicResult<CreateUserResponse> {
        val params = mutableMapOf("username" to username, "password" to encodePassword(password), "email" to email)
        ldapAuthenticated?.let { params["ldapAuthenticated"] = it.toString() }
        adminRole?.let { params["adminRole"] = it.toString() }
        settingsRole?.let { params["settingsRole"] = it.toString() }
        streamRole?.let { params["streamRole"] = it.toString() }
        jukeboxRole?.let { params["jukeboxRole"] = it.toString() }
        downloadRole?.let { params["downloadRole"] = it.toString() }
        uploadRole?.let { params["uploadRole"] = it.toString() }
        playlistRole?.let { params["playlistRole"] = it.toString() }
        coverArtRole?.let { params["coverArtRole"] = it.toString() }
        commentRole?.let { params["commentRole"] = it.toString() }
        podcastRole?.let { params["podcastRole"] = it.toString() }
        shareRole?.let { params["shareRole"] = it.toString() }
        videoConversionRole?.let { params["videoConversionRole"] = it.toString() }
        val multiValueParams = if (musicFolderIds.isNotEmpty()) mapOf("musicFolderId" to musicFolderIds) else emptyMap()
        return executor.execute("createUser.view", params, multiValueParams) { jsonObject ->
            CreateUserParser.parse(jsonObject)
        }
    }

    /**
     * Modifies an existing user on the server.
     *
     * @param username The name of the user to update.
     * @param password The new password of the user.
     * @param email The new email address of the user.
     * @param ldapAuthenticated Whether the user is authenticated in LDAP.
     * @param adminRole Whether the user is administrator.
     * @param settingsRole Whether the user is allowed to change personal settings and password.
     * @param streamRole Whether the user is allowed to play files.
     * @param jukeboxRole Whether the user is allowed to play files in jukebox mode.
     * @param downloadRole Whether the user is allowed to download files.
     * @param uploadRole Whether the user is allowed to upload files.
     * @param coverArtRole Whether the user is allowed to change cover art and tags.
     * @param commentRole Whether the user is allowed to create and edit comments and ratings.
     * @param podcastRole Whether the user is allowed to administrate Podcasts.
     * @param shareRole Whether the user is allowed to share files with anyone.
     * @param videoConversionRole Whether the user is allowed to start video conversions.
     * @param musicFolderIds IDs of the music folders the user is allowed access to.
     * @param maxBitRate The maximum bit rate for the user. [MaxBitRate.NO_LIMIT] means no limit.
     * @return [SubsonicResult.Success] containing [UpdateUserResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or the user does not exist.
     */
    suspend fun updateUser(
        username: String,
        password: String? = null,
        email: String? = null,
        ldapAuthenticated: Boolean? = null,
        adminRole: Boolean? = null,
        settingsRole: Boolean? = null,
        streamRole: Boolean? = null,
        jukeboxRole: Boolean? = null,
        downloadRole: Boolean? = null,
        uploadRole: Boolean? = null,
        coverArtRole: Boolean? = null,
        commentRole: Boolean? = null,
        podcastRole: Boolean? = null,
        shareRole: Boolean? = null,
        videoConversionRole: Boolean? = null,
        musicFolderIds: List<String> = emptyList(),
        maxBitRate: MaxBitRate? = null
    ): SubsonicResult<UpdateUserResponse> {
        val params = mutableMapOf("username" to username)
        password?.let { params["password"] = encodePassword(it) }
        email?.let { params["email"] = it }
        ldapAuthenticated?.let { params["ldapAuthenticated"] = it.toString() }
        adminRole?.let { params["adminRole"] = it.toString() }
        settingsRole?.let { params["settingsRole"] = it.toString() }
        streamRole?.let { params["streamRole"] = it.toString() }
        jukeboxRole?.let { params["jukeboxRole"] = it.toString() }
        downloadRole?.let { params["downloadRole"] = it.toString() }
        uploadRole?.let { params["uploadRole"] = it.toString() }
        coverArtRole?.let { params["coverArtRole"] = it.toString() }
        commentRole?.let { params["commentRole"] = it.toString() }
        podcastRole?.let { params["podcastRole"] = it.toString() }
        shareRole?.let { params["shareRole"] = it.toString() }
        videoConversionRole?.let { params["videoConversionRole"] = it.toString() }
        maxBitRate?.let { params["maxBitRate"] = it.value.toString() }
        val multiValueParams = if (musicFolderIds.isNotEmpty()) mapOf("musicFolderId" to musicFolderIds) else emptyMap()
        return executor.execute("updateUser.view", params, multiValueParams) { jsonObject ->
            UpdateUserParser.parse(jsonObject)
        }
    }

    /**
     * Deletes an existing user on the server.
     *
     * @param username The name of the user to delete.
     * @return [SubsonicResult.Success] containing [DeleteUserResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or the user does not exist.
     */
    suspend fun deleteUser(username: String): SubsonicResult<DeleteUserResponse> {
        val params = mapOf("username" to username)
        return executor.execute("deleteUser.view", params) { jsonObject ->
            DeleteUserParser.parse(jsonObject)
        }
    }

    /**
     * Changes the password of an existing user.
     *
     * @param username The name of the user whose password to change.
     * @param password The new password.
     * @return [SubsonicResult.Success] containing [ChangePasswordResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails or the user does not exist.
     */
    suspend fun changePassword(username: String, password: String): SubsonicResult<ChangePasswordResponse> {
        val params = mapOf("username" to username, "password" to encodePassword(password))
        return executor.execute("changePassword.view", params) { jsonObject ->
            ChangePasswordParser.parse(jsonObject)
        }
    }

    /**
     * Returns all bookmarks for the current user.
     *
     * @return [SubsonicResult.Success] containing [BookmarksResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getBookmarks(): SubsonicResult<BookmarksResponse> {
        return executor.execute("getBookmarks.view") { jsonObject ->
            GetBookmarksParser.parse(jsonObject)
        }
    }

    /**
     * Creates or updates a bookmark for the given media file.
     *
     * If a bookmark already exists for the specified file it will be overwritten.
     *
     * @param id The ID of the media file to bookmark.
     * @param position The position in milliseconds within the media file.
     * @param comment An optional user-defined comment.
     * @return [SubsonicResult.Success] containing [CreateBookmarkResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun createBookmark(
        id: String,
        position: Long,
        comment: String? = null
    ): SubsonicResult<CreateBookmarkResponse> {
        val params = mutableMapOf("id" to id, "position" to position.toString())
        comment?.let { params["comment"] = it }
        return executor.execute("createBookmark.view", params) { jsonObject ->
            CreateBookmarkParser.parse(jsonObject)
        }
    }

    /**
     * Deletes the bookmark for the given media file.
     *
     * Only the current user's bookmark is affected.
     *
     * @param id The ID of the media file whose bookmark to delete.
     * @return [SubsonicResult.Success] containing [DeleteBookmarkResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun deleteBookmark(id: String): SubsonicResult<DeleteBookmarkResponse> {
        val params = mapOf("id" to id)
        return executor.execute("deleteBookmark.view", params) { jsonObject ->
            DeleteBookmarkParser.parse(jsonObject)
        }
    }

    /**
     * Returns the play queue for the current user.
     *
     * @return [SubsonicResult.Success] containing [PlayQueueResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getPlayQueue(): SubsonicResult<PlayQueueResponse> {
        return executor.execute("getPlayQueue.view") { jsonObject ->
            GetPlayQueueParser.parse(jsonObject)
        }
    }

    /**
     * Saves the play queue for the current user.
     *
     * @param ids The IDs of the songs in the play queue, in order.
     * @param current The ID of the currently playing song.
     * @param position The position in milliseconds within the currently playing song.
     * @return [SubsonicResult.Success] containing [SavePlayQueueResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun savePlayQueue(
        ids: List<String>,
        current: String? = null,
        position: Long? = null
    ): SubsonicResult<SavePlayQueueResponse> {
        val params = mutableMapOf<String, String>()
        current?.let { params["current"] = it }
        position?.let { params["position"] = it.toString() }
        val multiValueParams = if (ids.isNotEmpty()) mapOf("id" to ids) else emptyMap()
        return executor.execute("savePlayQueue.view", params, multiValueParams) { jsonObject ->
            SavePlayQueueParser.parse(jsonObject)
        }
    }

    /**
     * Returns the status of the media library scan.
     *
     * @return [SubsonicResult.Success] containing [ScanStatusResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getScanStatus(): SubsonicResult<ScanStatusResponse> {
        return executor.execute("getScanStatus.view") { jsonObject ->
            GetScanStatusParser.parse(jsonObject)
        }
    }

    /**
     * Initiates a rescan of the media library.
     *
     * @return [SubsonicResult.Success] containing [ScanStatusResponse] on success.
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun startScan(): SubsonicResult<ScanStatusResponse> {
        return executor.execute("startScan.view") { jsonObject ->
            StartScanParser.parse(jsonObject)
        }
    }
}
