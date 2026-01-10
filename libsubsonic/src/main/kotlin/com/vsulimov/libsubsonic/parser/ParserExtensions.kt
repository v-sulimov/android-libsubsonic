package com.vsulimov.libsubsonic.parser

import com.vsulimov.libsubsonic.data.Constants.DEFAULT_RESPONSE_STATUS
import com.vsulimov.libsubsonic.data.Constants.DEFAULT_VERSION_FALLBACK
import org.json.JSONObject

/**
 * Holds the common envelope fields present in every Subsonic response.
 *
 * @property status The status of the response.
 * @property apiVersion The Subsonic REST API version.
 * @property serverType The implementation type of the server.
 * @property serverVersion The specific version of the server software.
 * @property isOpenSubsonic Indicates if the server supports OpenSubsonic extensions.
 */
internal data class ResponseEnvelope(
    val status: String,
    val apiVersion: String,
    val serverType: String?,
    val serverVersion: String?,
    val isOpenSubsonic: Boolean
)

/**
 * Parses the common Subsonic response envelope fields from a "subsonic-response" [JSONObject].
 *
 * @return A [ResponseEnvelope] populated from the JSON fields.
 */
internal fun JSONObject.parseEnvelope(): ResponseEnvelope = ResponseEnvelope(
    status = optString("status", DEFAULT_RESPONSE_STATUS),
    apiVersion = optString("version", DEFAULT_VERSION_FALLBACK),
    serverType = optString("type").ifEmpty { null },
    serverVersion = optString("serverVersion").ifEmpty { null },
    isOpenSubsonic = optBoolean("openSubsonic", false)
)

/**
 * Parses a list of items from a JSON container, handling both JSON array and single-object cases.
 *
 * If the value at [key] is a JSON array, each element is passed to [parser] and non-null results
 * are collected. If the value is a single JSON object, [parser] is called once. If the key is
 * absent, an empty list is returned.
 *
 * @param key The JSON key identifying the array or object to parse.
 * @param parser A function transforming a [JSONObject] element into [T], or null to skip it.
 * @return The list of successfully parsed items.
 */
internal fun <T : Any> JSONObject.parseList(key: String, parser: (JSONObject) -> T?): List<T> {
    val result = mutableListOf<T>()
    val array = optJSONArray(key)
    if (array != null) {
        for (i in 0 until array.length()) {
            parser(array.getJSONObject(i))?.let { result.add(it) }
        }
    } else {
        optJSONObject(key)?.let { parser(it)?.let { item -> result.add(item) } }
    }
    return result
}
