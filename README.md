# libsubsonic

A lightweight, coroutine-based Android library for interacting with the Subsonic REST API.

Built entirely in Kotlin, libsubsonic covers the full surface of the Subsonic 1.16.1 API specification with a
consistent, type-safe interface that returns structured results.

---

## Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [Installation](#installation)
- [Getting Started](#getting-started)
- [Authentication](#authentication)
- [Result Handling](#result-handling)
- [Error Handling](#error-handling)
- [Binary Streaming](#binary-streaming)
- [Password Encoding](#password-encoding)
- [Enum Parameters](#enum-parameters)
- [API Reference](docs/API_REFERENCE.md)
- [Architecture](docs/ARCHITECTURE.md)
- [License](#license)

---

## Overview

libsubsonic provides a single public entry point — `SubsonicClient` — that exposes every endpoint of the Subsonic REST
API as a `suspend` function. All network I/O is dispatched to `Dispatchers.IO`, making every call safe to invoke from
any coroutine context, including the main thread.

The library handles:

- Token and salt generation for salted MD5 authentication
- URL construction and query parameter encoding
- JSON response parsing into typed Kotlin data classes
- Transparent password hex-encoding to avoid transmitting credentials in plain text
- Both structured JSON responses and raw binary streaming responses

The library targets **Subsonic API version 1.16.1** and uses ID3-tag-based endpoints where applicable (`getAlbumList2`,
`getArtistInfo2`, `getAlbumInfo2`, `getSimilarSongs2`, `getStarred2`, `search3`). These variants are the modern standard
and provide accurate metadata independent of file system structure.

---

## Requirements

- **Android**: minimum SDK 24 (Android 7.0 Nougat)
- **Kotlin**: coroutines support required in the consuming application
- **JVM target**: 11

---

## Installation

Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.vsulimov:libsubsonic:1.0.0-rc1")
}
```

Or in Groovy `build.gradle`:

```groovy
dependencies {
    implementation 'com.vsulimov:libsubsonic:1.0.0-rc1'
}
```

---

## Getting Started

Instantiate `SubsonicClient` with the base URL of your Subsonic server, then supply credentials before making any API
call.

```kotlin
val client = SubsonicClient(baseUrl = "https://music.example.com/")
client.setCredentials(username = "alice", password = "s3cr3t")
```

Connection and read timeouts can be configured via constructor parameters (defaults are 15 000 ms and 30 000 ms
respectively):

```kotlin
val client = SubsonicClient(
    baseUrl = "https://music.example.com/",
    connectTimeoutMs = 10_000,
    readTimeoutMs = 60_000
)
```

All API methods are `suspend` functions and must be called from within a coroutine scope.

```kotlin
viewModelScope.launch {
    when (val result = client.ping()) {
        is SubsonicResult.Success -> {
            val version = result.data.apiVersion
        }
        is SubsonicResult.Failure -> {
            val errorCode = result.error.code
            val message = result.error.message
        }
    }
}
```

---

## Authentication

libsubsonic uses the salted MD5 token authentication scheme introduced in Subsonic API 1.13.0. When credentials are set
via `setCredentials`, the library generates a random 8-character hex salt on every request and computes the MD5 hash of
the concatenation of the password and salt. The token and salt are appended to each request URL automatically.

Credentials are stored in memory within the URL builder and are never written to disk by the library. Calling
`setCredentials` at any point replaces the previous credentials for all subsequent requests.

---

## Result Handling

Every API method returns `SubsonicResult<T>`, a sealed interface with two variants:

```kotlin
sealed interface SubsonicResult<out T> {
    data class Success<T>(val data: T) : SubsonicResult<T>
    data class Failure(val error: SubsonicError) : SubsonicResult<Nothing>
}
```

`SubsonicResult.Success` carries the parsed response payload typed to the specific response class for that endpoint.
`SubsonicResult.Failure` carries a `SubsonicError` instance with a `SubsonicErrorCode` enum value and a human-readable message.

The idiomatic way to consume a result is a `when` expression:

```kotlin
when (val result = client.getArtists()) {
    is SubsonicResult.Success -> renderArtists(result.data.artists)
    is SubsonicResult.Failure -> showError(result.error.message)
}
```

---

## Error Handling

`SubsonicError` extends `Exception` and contains two properties:

```kotlin
data class SubsonicError(val code: SubsonicErrorCode, override val message: String) : Exception(message)
```

Stack trace capture is skipped for performance since `SubsonicError` serves as a structured error carrier rather than a
throwable intended for debugging call sites.

The `code` field is a `SubsonicErrorCode` enum corresponding to the Subsonic API error codes:

| Enum Entry                 | Code | Meaning                                         |
|----------------------------|------|-------------------------------------------------|
| `GENERIC_ERROR`            | 0    | A generic error                                 |
| `MISSING_PARAMETER`        | 10   | Required parameter is missing                   |
| `INCOMPATIBLE_VERSION`     | 20   | Incompatible Subsonic REST API protocol version |
| `WRONG_CREDENTIALS_LEGACY` | 30   | Wrong username or password (legacy auth)        |
| `WRONG_CREDENTIALS`        | 40   | Wrong username or password (token auth)         |
| `TOKEN_AUTH_NOT_SUPPORTED`  | 41   | Token authentication not supported              |
| `NOT_AUTHORIZED`           | 50   | User is not authorized for the given operation  |
| `TRIAL_EXPIRED`            | 60   | Trial period for Subsonic server is over        |
| `DATA_NOT_FOUND`           | 70   | Requested data not found                        |

You can match on specific error codes directly:

```kotlin
when (result.error.code) {
    SubsonicErrorCode.WRONG_CREDENTIALS -> promptForPassword()
    SubsonicErrorCode.NOT_AUTHORIZED -> showPermissionDenied()
    SubsonicErrorCode.DATA_NOT_FOUND -> showNotFound()
    else -> showGenericError(result.error.message)
}
```

Network failures, configuration errors, and unexpected exceptions are also surfaced as `SubsonicResult.Failure`, with
`SubsonicErrorCode.GENERIC_ERROR` and the original exception message.

---

## Binary Streaming

Methods that return raw binary data — `stream`, `download`, `hls`, `getCaptions`, `getCoverArt`, and `getAvatar` —
accept a `responseHandler: suspend (InputStream) -> Unit` lambda instead of returning a parsed object. The lambda
receives the raw HTTP response body as an `InputStream`.

Because the handler is a `suspend` function, callers can use coroutine-based I/O inside it (for example, writing to a
file with `withContext(Dispatchers.IO)`). The network connection is held open for the duration of the lambda execution.

```kotlin
client.getCoverArt(id = "al-1", size = 300) { inputStream ->
    val bitmap = BitmapFactory.decodeStream(inputStream)
    // use bitmap
}
```

All streaming methods return `SubsonicResult<Unit>`. A `Success` result indicates that the HTTP request completed and
the response handler ran without throwing. A `Failure` result indicates a network error or server error that occurred
before the response body became available.

---

## Password Encoding

The Subsonic API supports sending passwords either in clear text or encoded as a hex string prefixed with `enc:`.
libsubsonic always uses hex encoding. When `createUser`, `updateUser`, or `changePassword` is called, the library
converts the supplied password to its UTF-8 byte representation, encodes those bytes as a lowercase hexadecimal string,
and prepends the `enc:` prefix before including the value in the request URL.

This encoding is automatic and transparent. The caller always passes the plain-text password; the library handles the
encoding internally.

---

## Enum Parameters

Several API parameters accept only a predefined set of legal values. These are represented as Kotlin enums with a
`value` property that holds the wire-format string or integer the API expects.

| Enum            | Used by          | Values                                                                                                                                  |
|-----------------|------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| `AlbumListType` | `getAlbumList`   | `RANDOM`, `NEWEST`, `HIGHEST`, `FREQUENT`, `RECENT`, `STARRED`, `ALPHABETICAL_BY_NAME`, `ALPHABETICAL_BY_ARTIST`, `BY_YEAR`, `BY_GENRE` |
| `Rating`        | `setRating`      | `REMOVE` (0), `ONE` through `FIVE` (1–5)                                                                                                |
| `JukeboxAction` | `jukeboxControl` | `GET`, `STATUS`, `SET`, `START`, `STOP`, `SKIP`, `ADD`, `CLEAR`, `REMOVE`, `SHUFFLE`, `SET_GAIN`                                        |
| `MaxBitRate`    | `updateUser`     | `NO_LIMIT`, `KBPS_32` through `KBPS_320`                                                                                                |

---

## License

Copyright (c) 2026 Vitaly Sulimov

Licensed under the MIT License. See [LICENSE](LICENSE) for full details.
