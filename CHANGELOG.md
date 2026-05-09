# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0-rc2] - 2026-05-09

### Added

- `SubsonicClient.streamUrl(...)` — returns a fully signed `stream.view` URL ready to hand to a third-party media player such as ExoPlayer or `MediaPlayer`, with no additional I/O required.
- `PodcastStatus` enum (`NEW`, `DOWNLOADING`, `COMPLETED`, `ERROR`, `DELETED`, `SKIPPED`, `UNKNOWN`) — used by `PodcastChannel.status` and `PodcastEpisode.status`.
- `CaptionsFormat` enum (`SRT`, `VTT`) — used by `SubsonicClient.getCaptions(format)`.
- `StreamFormat` sealed class (`Raw`, `Custom(value)`) — used by `SubsonicClient.stream(format)` and `SubsonicClient.streamUrl(format)`.

### Changed

- **Breaking:** `PodcastChannel.status` and `PodcastEpisode.status` are now typed as `PodcastStatus` instead of `String`.
- **Breaking:** `SubsonicClient.stream(format)` parameter is now `StreamFormat?` instead of `String?`.
- **Breaking:** `SubsonicClient.getCaptions(format)` parameter is now `CaptionsFormat?` instead of `String?`.
- **Breaking:** `Genre.value` renamed to `Genre.name` for clarity (the field has always represented the genre's display name).
- **Breaking:** `SubsonicAuthenticator`, `HttpClient`, `GetRequest`, `HttpResponse`, and `StreamingHttpResponse` are now `internal`. They were never intended for direct use; consumers should rely on the public `SubsonicClient` API.
- **Breaking:** Default timeout constants moved from `HttpClient.Companion.DEFAULT_CONNECT_TIMEOUT_MS` / `DEFAULT_READ_TIMEOUT_MS` to `com.vsulimov.libsubsonic.data.Constants.DEFAULT_CONNECT_TIMEOUT_MS` / `DEFAULT_READ_TIMEOUT_MS`.

### Fixed

- README incorrectly listed `HIGHEST` as an `AlbumListType` value. The Subsonic `getAlbumList2` endpoint does not support that value, and the enum has never exposed it.

### Build

- Toolchain refresh: AGP `9.2.1`, Kotlin `2.3.21`, `kotlinx-coroutines-core` `1.11.0`, Gradle wrapper `9.5.0`, `compileSdk` `37`. Runtime requirements unchanged (`minSdk = 24`, JVM 11).

## [1.0.0-rc1] - Initial release

First release candidate covering the full Subsonic REST API 1.16.1 surface.
