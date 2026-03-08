# Architecture

## Package Structure

The library is organized into packages that mirror the logical groupings of the Subsonic API:

```
com.vsulimov.libsubsonic
├── SubsonicClient.kt               — public entry point
├── auth/                           — authentication and password encoding
├── data/
│   ├── response/
│   │   ├── annotation/             — star, rating, scrobble response types
│   │   ├── bookmark/               — bookmark response types
│   │   ├── browsing/               — artist, album, song, genre response types
│   │   ├── chat/                   — chat message response types
│   │   ├── internetradio/          — internet radio station response types
│   │   ├── jukebox/                — jukebox status response types
│   │   ├── lists/                  — album list, now playing, starred response types
│   │   ├── lyrics/                 — lyrics response types
│   │   ├── playlists/              — playlist response types
│   │   ├── playqueue/              — play queue response types
│   │   ├── podcast/                — podcast channel and episode response types
│   │   ├── scan/                   — scan status response types
│   │   ├── search/                 — search result response types
│   │   ├── sharing/                — share response types
│   │   ├── system/                 — ping and license response types
│   │   ├── user/                   — user response types and MaxBitRate enum
│   │   └── video/                  — video and video info response types
│   └── result/
│       ├── SubsonicResult.kt       — sealed result type
│       └── error/SubsonicError.kt  — error data class
├── executor/
│   └── SubsonicRequestExecutor.kt  — HTTP dispatch and coroutine bridging
├── http/                           — lightweight HTTP client with configurable timeouts
├── parser/                         — internal JSON parsers organized per endpoint group
├── url/
│   └── SubsonicUrlBuilder.kt       — URL construction and credential management
└── data/Constants.kt               — shared constants
```

## Request Execution

`SubsonicRequestExecutor` is the internal component responsible for executing HTTP requests. It constructs signed URLs
including authentication parameters, performs the GET request, and delegates the raw response to a parser lambda.

All blocking network calls are wrapped in `withContext(Dispatchers.IO)`, making every public method on `SubsonicClient`
safe to call from any coroutine dispatcher.

Two execution paths exist:

- `execute(endpoint, params, multiValueParams, dataParser)` — for endpoints that return a JSON body. Parses the Subsonic
  response envelope and delegates the payload to the provided `dataParser` lambda.
- `executeStreaming(endpoint, params, multiValueParams, responseHandler)` — for endpoints that return a binary body.
  Passes the raw `InputStream` to the `responseHandler` lambda without any JSON parsing.

## JSON Parsing

Each endpoint group has a dedicated internal parser object under `com.vsulimov.libsubsonic.parser`. Parsers use
`org.json.JSONObject`, which is available on Android without additional dependencies.

Where a nested object type is shared across multiple responses — for example, `Child` for song entries used across song
lists, bookmarks, play queues, and playlists — the reusable parsing logic is extracted as an `internal fun` within the
originating parser and called from each dependent parser.

## Response Envelope

Every Subsonic JSON response shares a common envelope:

```json
{
  "subsonic-response": {
    "status": "ok",
    "version": "1.16.1",
    "type": "navidrome",
    "serverVersion": "0.50.0",
    "openSubsonic": true,
    ...
  }
}
```

The `parseEnvelope()` extension function extracts `status`, `apiVersion`, `serverType`, `serverVersion`, and
`isOpenSubsonic` from the root `JSONObject`. All response types carry these five fields in addition to their specific
payload.

## Multi-Value Parameters

Some API endpoints accept a parameter key that may be repeated multiple times in the query string — for example,
`id=1&id=2&id=3` for batch operations such as `star`, `createPlaylist`, or `savePlayQueue`. These are passed to the
executor as `Map<String, List<String>>` and serialized into repeated query pairs by the URL builder.
