package com.vsulimov.libsubsonic.parser

import com.vsulimov.libsubsonic.data.Constants.DEFAULT_RESPONSE_STATUS
import com.vsulimov.libsubsonic.data.Constants.DEFAULT_VERSION_FALLBACK
import org.json.JSONObject

/**
 * The metadata fields shared by every Subsonic response envelope.
 *
 * @property status The status of the response (typically `"ok"`).
 * @property apiVersion The Subsonic REST API version reported by the server.
 * @property serverType The server implementation (e.g. `"navidrome"`), or `null` if not reported.
 * @property serverVersion The server software version, or `null` if not reported.
 * @property isOpenSubsonic Whether the server advertises OpenSubsonic extensions.
 */
internal data class ResponseEnvelope(
    val status: String,
    val apiVersion: String,
    val serverType: String?,
    val serverVersion: String?,
    val isOpenSubsonic: Boolean
)

/**
 * Reads the common Subsonic envelope fields from this `subsonic-response` [JSONObject].
 *
 * @return A populated [ResponseEnvelope]; missing string fields fall back to
 *   [DEFAULT_RESPONSE_STATUS] / [DEFAULT_VERSION_FALLBACK], and missing nullable
 *   fields are returned as `null`.
 */
internal fun JSONObject.parseEnvelope(): ResponseEnvelope = ResponseEnvelope(
    status = optString("status", DEFAULT_RESPONSE_STATUS),
    apiVersion = optString("version", DEFAULT_VERSION_FALLBACK),
    serverType = optString("type").ifEmpty { null },
    serverVersion = optString("serverVersion").ifEmpty { null },
    isOpenSubsonic = optBoolean("openSubsonic", false)
)

/**
 * Convenience builder for endpoints whose response is the envelope alone — no payload fields.
 *
 * Reads the envelope from this `subsonic-response` JSON and forwards each field to [construct],
 * which is typically the response data class's constructor reference (e.g. `::PingResponse`).
 *
 * @param construct Function receiving the five envelope fields in declaration order
 *   (`status`, `apiVersion`, `serverType`, `serverVersion`, `isOpenSubsonic`) and returning [R].
 * @return The response value produced by [construct].
 */
internal inline fun <R> JSONObject.envelopeOnly(
    construct: (
        status: String,
        apiVersion: String,
        serverType: String?,
        serverVersion: String?,
        isOpenSubsonic: Boolean
    ) -> R
): R {
    val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = parseEnvelope()
    return construct(status, apiVersion, serverType, serverVersion, isOpenSubsonic)
}

/**
 * Parses a list of items from a JSON container, accepting both the JSON-array form and the
 * single-object shorthand the Subsonic API uses when only one item is present.
 *
 * If the value at [key] is a JSON array, every element is mapped through [parser] and
 * non-`null` results are collected. If the value is a single JSON object, [parser] is invoked
 * once. If the key is absent, an empty list is returned.
 *
 * @param key The JSON key identifying the array or object to parse.
 * @param parser Function mapping a [JSONObject] element to [T], returning `null` to skip it.
 * @return The list of successfully parsed items in source order.
 */
internal fun <T : Any> JSONObject.parseList(key: String, parser: (JSONObject) -> T?): List<T> {
    optJSONArray(key)?.let { array ->
        return (0 until array.length()).mapNotNull { i -> parser(array.getJSONObject(i)) }
    }
    return optJSONObject(key)?.let(parser)?.let(::listOf) ?: emptyList()
}

/**
 * Returns the string value at [key], or `null` if the key is missing or the value is empty.
 *
 * Equivalent to `optString(key).ifEmpty { null }`. Empty strings are treated as absent because
 * `org.json` reports both missing keys and JSON `null` as the empty string from [optString].
 */
internal fun JSONObject.optStringOrNull(key: String): String? = optString(key).ifEmpty { null }

/**
 * Returns the int value at [key], or `null` if the key is absent.
 *
 * Note: a JSON `null` is reported as present by [JSONObject.has] but coerces to `0` via
 * [JSONObject.optInt]; that quirk is preserved to match the existing parser behaviour.
 */
internal fun JSONObject.optIntOrNull(key: String): Int? = if (has(key)) optInt(key) else null

/**
 * Returns the long value at [key], or `null` if the key is absent.
 */
internal fun JSONObject.optLongOrNull(key: String): Long? = if (has(key)) optLong(key) else null

/**
 * Returns the double value at [key], or `null` if the key is absent.
 */
internal fun JSONObject.optDoubleOrNull(key: String): Double? = if (has(key)) optDouble(key) else null

/**
 * Returns the JSON string-array at [key] as a Kotlin [List], or an empty list if the key is absent.
 *
 * Each element is read with [org.json.JSONArray.getString], which throws if any element is not a
 * string — that behaviour is intentional, since a non-string element indicates a malformed payload.
 */
internal fun JSONObject.optStringList(key: String): List<String> {
    val array = optJSONArray(key) ?: return emptyList()
    return (0 until array.length()).map { array.getString(it) }
}
