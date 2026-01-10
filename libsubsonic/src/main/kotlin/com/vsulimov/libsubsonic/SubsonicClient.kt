package com.vsulimov.libsubsonic

import com.vsulimov.libsubsonic.data.Constants.DEFAULT_CLIENT_NAME
import com.vsulimov.libsubsonic.data.response.browsing.AlbumResponse
import com.vsulimov.libsubsonic.data.response.browsing.ArtistResponse
import com.vsulimov.libsubsonic.data.response.browsing.ArtistsResponse
import com.vsulimov.libsubsonic.data.response.browsing.GenresResponse
import com.vsulimov.libsubsonic.data.response.browsing.IndexesResponse
import com.vsulimov.libsubsonic.data.response.browsing.MusicDirectoryResponse
import com.vsulimov.libsubsonic.data.response.browsing.MusicFoldersResponse
import com.vsulimov.libsubsonic.data.response.browsing.SongResponse
import com.vsulimov.libsubsonic.data.response.system.PingResponse
import com.vsulimov.libsubsonic.data.result.SubsonicResult
import com.vsulimov.libsubsonic.executor.SubsonicRequestExecutor
import com.vsulimov.libsubsonic.parser.browsing.GetAlbumParser
import com.vsulimov.libsubsonic.parser.browsing.GetArtistParser
import com.vsulimov.libsubsonic.parser.browsing.GetArtistsParser
import com.vsulimov.libsubsonic.parser.browsing.GetGenresParser
import com.vsulimov.libsubsonic.parser.browsing.GetIndexesParser
import com.vsulimov.libsubsonic.parser.browsing.GetMusicDirectoryParser
import com.vsulimov.libsubsonic.parser.browsing.GetMusicFoldersParser
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.system.PingParser
import com.vsulimov.libsubsonic.url.SubsonicUrlBuilder

/**
 * The entry point for the Subsonic API client library.
 *
 * This client provides a coroutine-based interface to interact with Subsonic server.
 * It handles authentication, URL signing, and JSON parsing internally.
 *
 * @param baseUrl The full URL to the Subsonic server (e.g., "http://myserver.com/").
 * @param clientName A unique identifier for this client application.
 */
class SubsonicClient(baseUrl: String, clientName: String = DEFAULT_CLIENT_NAME) {

    // Internal components composed here.
    private val urlBuilder = SubsonicUrlBuilder(baseUrl, clientName)
    private val executor = SubsonicRequestExecutor(urlBuilder)

    /**
     * Sets the user credentials for future API calls.
     *
     * This method stores the credentials in memory to generate unique authentication
     * tokens (using Salt + MD5) for subsequent requests.
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
     * @return [SubsonicResult.Success] containing [SongResponse].
     * @return [SubsonicResult.Failure] if the request fails.
     */
    suspend fun getSong(id: String): SubsonicResult<SongResponse> {
        val params = mapOf("id" to id)
        return executor.execute("getSong.view", params) { jsonObject ->
            GetSongParser.parse(jsonObject)
        }
    }
}
