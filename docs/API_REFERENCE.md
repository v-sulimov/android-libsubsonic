# API Reference

## System

| Method         | Description                                                           |
|----------------|-----------------------------------------------------------------------|
| `ping()`       | Pings the server to verify connectivity and retrieve server metadata. |
| `getLicense()` | Returns the current license information for the Subsonic server.      |

## Library Browsing

| Method                                        | Description                                                                                          |
|-----------------------------------------------|------------------------------------------------------------------------------------------------------|
| `getMusicFolders()`                           | Returns all configured top-level music folders.                                                      |
| `getIndexes(musicFolderId, ifModifiedSince)`  | Returns an indexed list of artists, optionally filtered by music folder and modification time.       |
| `getMusicDirectory(id)`                       | Returns the contents of a specific music directory for folder-based browsing.                        |
| `getGenres()`                                 | Returns all genres, including song and album counts per genre.                                       |
| `getArtists(musicFolderId)`                   | Returns a list of all artists grouped alphabetically using ID3 tags.                                 |
| `getArtist(id)`                               | Returns details for an artist, including a list of their albums.                                     |
| `getAlbum(id)`                                | Returns details for an album, including its song list.                                               |
| `getSong(id)`                                 | Returns details for a specific song.                                                                 |
| `getVideos()`                                 | Returns all video files available on the server.                                                     |
| `getVideoInfo(id)`                            | Returns detailed metadata for a video, including audio tracks and conversion profiles.               |
| `getArtistInfo(id, count, includeNotPresent)` | Returns extended artist metadata such as biography and similar artists. Uses the ID3-based endpoint. |
| `getAlbumInfo(id)`                            | Returns extended album metadata such as editorial notes. Uses the ID3-based endpoint.                |
| `getSimilarSongs(id, count)`                  | Returns a list of songs similar to a given song. Uses the ID3-based endpoint.                        |
| `getTopSongs(artistName, count)`              | Returns the top songs for a given artist.                                                            |

## Album and Song Lists

| Method                                                                     | Description                                                                                             |
|----------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| `getAlbumList(type, size, offset, fromYear, toYear, genre, musicFolderId)` | Returns a list of albums using a specified ordering strategy.                                           |
| `getRandomSongs(size, genre, fromYear, toYear, musicFolderId)`             | Returns a randomly selected set of songs.                                                               |
| `getSongsByGenre(genre, count, offset, musicFolderId)`                     | Returns songs filtered by genre.                                                                        |
| `getNowPlaying()`                                                          | Returns what is currently being played by all users on the server.                                      |
| `getStarred(musicFolderId)`                                                | Returns all starred songs, albums, and artists for the authenticated user. Uses the ID3-based endpoint. |

`getAlbumList` accepts an `AlbumListType` enum value to specify the sort strategy. Available values include `RANDOM`,
`NEWEST`, `HIGHEST`, `FREQUENT`, `RECENT`, `STARRED`, `ALPHABETICAL_BY_NAME`, `ALPHABETICAL_BY_ARTIST`, `BY_YEAR`, and
`BY_GENRE`.

## Search

| Method                                                                                                    | Description                                                                                                                                |
|-----------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `search(query, artistCount, artistOffset, albumCount, albumOffset, songCount, songOffset, musicFolderId)` | Searches for artists, albums, and songs using ID3 tags. Supports independent pagination for each entity type. Uses the `search3` endpoint. |

## Playlists

| Method                                                                                 | Description                                                                                                                                |
|----------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `getPlaylists(username)`                                                               | Returns all playlists the authenticated user is allowed to play. Admin users may pass a `username` to retrieve playlists for another user. |
| `getPlaylist(id)`                                                                      | Returns a playlist with its complete song list.                                                                                            |
| `createPlaylist(playlistId, name, songIds)`                                            | Creates a new playlist or replaces the songs in an existing one. Either `playlistId` or `name` must be provided.                           |
| `updatePlaylist(playlistId, name, comment, public, songIdsToAdd, songIndexesToRemove)` | Updates metadata and song contents of an existing playlist.                                                                                |
| `deletePlaylist(id)`                                                                   | Deletes an existing playlist.                                                                                                              |

## Media Retrieval

| Method                                                                                                | Description                                                                                                                              |
|-------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| `stream(id, maxBitRate, format, timeOffset, size, estimateContentLength, converted, responseHandler)` | Streams a media file, with optional server-side transcoding to a specified format and bitrate.                                           |
| `download(id, responseHandler)`                                                                       | Downloads the original file without any transcoding.                                                                                     |
| `hls(id, bitRates, audioTrack, responseHandler)`                                                      | Generates an HLS (HTTP Live Streaming) M3U8 playlist. Multiple bitrates produce an adaptive variant playlist suitable for ABR streaming. |
| `getCaptions(id, format, responseHandler)`                                                            | Returns captions or subtitles for a video file in SRT or VTT format.                                                                     |
| `getCoverArt(id, size, responseHandler)`                                                              | Returns the cover art image for a song, album, or artist.                                                                                |
| `getLyrics(artist, title)`                                                                            | Returns song lyrics, optionally looked up by artist and title.                                                                           |
| `getAvatar(username, responseHandler)`                                                                | Returns the avatar image for a user.                                                                                                     |

## Media Annotation

| Method                             | Description                                                                                                                                                 |
|------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `star(ids, albumIds, artistIds)`   | Attaches a star to songs, albums, or artists. Accepts multiple IDs of each type in a single call.                                                           |
| `unstar(ids, albumIds, artistIds)` | Removes the star from songs, albums, or artists.                                                                                                            |
| `setRating(id, rating)`            | Sets a 1–5 star rating for a media file. Use `Rating.REMOVE` to clear an existing rating.                                                                   |
| `scrobble(ids, times, submission)` | Registers playback with the server for Last.fm scrobbling or "now playing" notification. Each timestamp in `times` corresponds to the ID at the same index. |

## Sharing

| Method                                   | Description                                                                                                                                |
|------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `getShares()`                            | Returns all shares created by the authenticated user.                                                                                      |
| `createShare(ids, description, expires)` | Creates a public share for one or more media files with an optional description and expiry timestamp in milliseconds since the Unix epoch. |
| `updateShare(id, description, expires)`  | Updates the description or expiry time of an existing share.                                                                               |
| `deleteShare(id)`                        | Deletes an existing share.                                                                                                                 |

## Podcast

| Method                             | Description                                                                                                                              |
|------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| `getPodcasts(includeEpisodes, id)` | Returns all podcast channels subscribed to by the server, optionally including episode lists. A specific channel may be requested by ID. |
| `getNewestPodcasts(count)`         | Returns the most recently published podcast episodes across all channels.                                                                |
| `refreshPodcasts()`                | Instructs the server to check all channels for new episodes.                                                                             |
| `createPodcastChannel(url)`        | Adds a new podcast channel by RSS feed URL.                                                                                              |
| `deletePodcastChannel(id)`         | Deletes a podcast channel and all of its associated episodes.                                                                            |
| `deletePodcastEpisode(id)`         | Deletes a specific podcast episode from the server.                                                                                      |
| `downloadPodcastEpisode(id)`       | Instructs the server to download a specific podcast episode for local storage.                                                           |

## Jukebox

| Method                                             | Description                                                                                                                      |
|----------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| `jukeboxControl(action, index, offset, ids, gain)` | Controls the jukebox player on the server. The `action` parameter is a `JukeboxAction` enum specifying the operation to perform. |

Supported `JukeboxAction` values: `GET`, `STATUS`, `SET`, `START`, `STOP`, `SKIP`, `ADD`, `CLEAR`, `REMOVE`, `SHUFFLE`,
`SET_GAIN`.

## Internet Radio

| Method                                                         | Description                                                             |
|----------------------------------------------------------------|-------------------------------------------------------------------------|
| `getInternetRadioStations()`                                   | Returns all configured internet radio stations.                         |
| `createInternetRadioStation(streamUrl, name, homePageUrl)`     | Creates a new internet radio station.                                   |
| `updateInternetRadioStation(id, streamUrl, name, homePageUrl)` | Updates the stream URL, name, and home page URL of an existing station. |
| `deleteInternetRadioStation(id)`                               | Deletes an internet radio station.                                      |

## Chat

| Method                    | Description                                                                                                                                   |
|---------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| `getChatMessages(since)`  | Returns chat messages. The optional `since` parameter is a Unix timestamp in milliseconds; only messages posted after this time are returned. |
| `addChatMessage(message)` | Posts a new chat message visible to all users on the server.                                                                                  |

## User Management

User management endpoints require the authenticated user to have the admin role, except for `changePassword` which
allows non-admin users to change their own password.

| Method                                       | Description                                                                                                             |
|----------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| `getUser(username)`                          | Returns information about a specific user, including assigned roles and music folders.                                  |
| `getUsers()`                                 | Returns information about all users on the server.                                                                      |
| `createUser(username, password, email, ...)` | Creates a new user with specified roles and settings.                                                                   |
| `updateUser(username, password, email, ...)` | Updates settings and roles for an existing user. Only the provided parameters are changed.                              |
| `deleteUser(username)`                       | Deletes an existing user account.                                                                                       |
| `changePassword(username, password)`         | Changes the password for a user. Admin users may change any user's password; non-admin users may only change their own. |

Passwords in `createUser`, `updateUser`, and `changePassword` are automatically hex-encoded by the library before
transmission. See [Password Encoding](../README.md#password-encoding) for details.

`updateUser` accepts an optional `maxBitRate` parameter of type `MaxBitRate`, an enum that constrains the input to legal
values defined by the API specification: `NO_LIMIT`, `KBPS_32`, `KBPS_40`, `KBPS_48`, `KBPS_56`, `KBPS_64`, `KBPS_80`,
`KBPS_96`, `KBPS_112`, `KBPS_128`, `KBPS_160`, `KBPS_192`, `KBPS_224`, `KBPS_256`, `KBPS_320`.

## Bookmarks

| Method                                  | Description                                                                                                                                             |
|-----------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| `getBookmarks()`                        | Returns all bookmarks for the authenticated user. Each bookmark carries the playback position and the full `Child` entry for the associated media file. |
| `createBookmark(id, position, comment)` | Creates or updates a bookmark for a media file at a given position in milliseconds.                                                                     |
| `deleteBookmark(id)`                    | Deletes the bookmark associated with a media file.                                                                                                      |

## Play Queue

| Method                                  | Description                                                                                                                                             |
|-----------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| `getPlayQueue()`                        | Returns the saved play queue for the authenticated user. The `playQueue` field in the response is `null` if no queue has been saved.                    |
| `savePlayQueue(ids, current, position)` | Saves the current play queue to the server. Accepts a list of song IDs, an optional current-song ID, and an optional playback position in milliseconds. |

## Media Library Scanning

| Method            | Description                                                                                                                    |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------|
| `getScanStatus()` | Returns the current media library scan status, including whether a scan is in progress and the number of items indexed so far. |
| `startScan()`     | Initiates a media library scan on the server and returns the initial scan status immediately.                                  |
