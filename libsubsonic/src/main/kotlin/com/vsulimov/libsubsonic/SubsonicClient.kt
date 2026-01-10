package com.vsulimov.libsubsonic

import com.vsulimov.libsubsonic.data.Constants.DEFAULT_CLIENT_NAME
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
import com.vsulimov.libsubsonic.data.response.lists.AlbumListResponse
import com.vsulimov.libsubsonic.data.response.lists.AlbumListType
import com.vsulimov.libsubsonic.data.response.lists.NowPlayingResponse
import com.vsulimov.libsubsonic.data.response.lists.RandomSongsResponse
import com.vsulimov.libsubsonic.data.response.lists.SongsByGenreResponse
import com.vsulimov.libsubsonic.data.response.lists.StarredResponse
import com.vsulimov.libsubsonic.data.response.playlists.DeletePlaylistResponse
import com.vsulimov.libsubsonic.data.response.playlists.PlaylistResponse
import com.vsulimov.libsubsonic.data.response.playlists.PlaylistsResponse
import com.vsulimov.libsubsonic.data.response.playlists.UpdatePlaylistResponse
import com.vsulimov.libsubsonic.data.response.search.SearchResponse
import com.vsulimov.libsubsonic.data.response.system.LicenseResponse
import com.vsulimov.libsubsonic.data.response.system.PingResponse
import com.vsulimov.libsubsonic.data.response.video.VideoInfoResponse
import com.vsulimov.libsubsonic.data.response.video.VideosResponse
import com.vsulimov.libsubsonic.data.result.SubsonicResult
import com.vsulimov.libsubsonic.executor.SubsonicRequestExecutor
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
import com.vsulimov.libsubsonic.parser.lists.GetAlbumListParser
import com.vsulimov.libsubsonic.parser.lists.GetNowPlayingParser
import com.vsulimov.libsubsonic.parser.lists.GetRandomSongsParser
import com.vsulimov.libsubsonic.parser.lists.GetSongsByGenreParser
import com.vsulimov.libsubsonic.parser.lists.GetStarredParser
import com.vsulimov.libsubsonic.parser.playlists.CreatePlaylistParser
import com.vsulimov.libsubsonic.parser.playlists.DeletePlaylistParser
import com.vsulimov.libsubsonic.parser.playlists.GetPlaylistParser
import com.vsulimov.libsubsonic.parser.playlists.GetPlaylistsParser
import com.vsulimov.libsubsonic.parser.playlists.UpdatePlaylistParser
import com.vsulimov.libsubsonic.parser.search.SearchParser
import com.vsulimov.libsubsonic.parser.system.GetLicenseParser
import com.vsulimov.libsubsonic.parser.system.PingParser
import com.vsulimov.libsubsonic.parser.video.GetVideoInfoParser
import com.vsulimov.libsubsonic.parser.video.GetVideosParser
import com.vsulimov.libsubsonic.url.SubsonicUrlBuilder

/**
 * Entry point for the Subsonic REST API client.
 *
 * This client provides a coroutine-based interface to a Subsonic server while
 * encapsulating authentication, URL signing, and JSON parsing.
 *
 * @param baseUrl The full URL to the Subsonic server (for example, "http://myserver.com/").
 * @param clientName A unique identifier for this client application.
 */
class SubsonicClient(baseUrl: String, clientName: String = DEFAULT_CLIENT_NAME) {

    private val urlBuilder = SubsonicUrlBuilder(baseUrl, clientName)
    private val executor = SubsonicRequestExecutor(urlBuilder)

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
     * @return [SubsonicResult.Success] containing [ArtistInfoResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getArtistInfo(id: String): SubsonicResult<ArtistInfoResponse> {
        val params = mapOf("id" to id)
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
}
