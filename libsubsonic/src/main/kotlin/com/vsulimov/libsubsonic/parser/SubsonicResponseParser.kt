package com.vsulimov.libsubsonic.parser

import com.vsulimov.libsubsonic.data.result.SubsonicResult
import com.vsulimov.libsubsonic.data.result.error.SubsonicError
import com.vsulimov.libsubsonic.data.result.error.SubsonicErrorCode
import org.json.JSONException
import org.json.JSONObject

/**
 * Parses raw Subsonic JSON response bodies into typed [SubsonicResult] values.
 *
 * Unwraps the standard `subsonic-response` envelope, intercepts API-level failures
 * (`status="failed"`) into [SubsonicResult.Failure] and delegates payload parsing
 * to the caller-supplied lambda.
 */
internal class SubsonicResponseParser {

    /**
     * Parses a raw JSON response body.
     *
     * @param jsonString The raw response body received from the server.
     * @param parser Lambda that converts the unwrapped `subsonic-response` JSON into [T].
     * @return [SubsonicResult.Success] with the parsed payload on a successful envelope, or
     *   [SubsonicResult.Failure] if the body is empty, malformed, missing the envelope, carries
     *   `status="failed"`, or the parser lambda throws.
     */
    fun <T> parse(jsonString: String, parser: (JSONObject) -> T): SubsonicResult<T> {
        if (jsonString.isBlank()) return genericFailure("Empty response received")

        return try {
            val body = JSONObject(jsonString).optJSONObject("subsonic-response")
                ?: return genericFailure("Invalid JSON: Missing 'subsonic-response' root")

            if ("failed".equals(body.optString("status"), ignoreCase = true)) {
                return extractApiError(body)
            }

            SubsonicResult.Success(parser(body))
        } catch (e: JSONException) {
            genericFailure("JSON parsing error: ${e.message}")
        } catch (e: Exception) {
            genericFailure("Unknown parsing error: ${e.message}")
        }
    }

    /**
     * Builds a [SubsonicResult.Failure] from the `error` object in a failed response envelope,
     * defaulting to [SubsonicErrorCode.GENERIC_ERROR] and a placeholder message when the server
     * omits the field or supplies an empty value.
     */
    private fun extractApiError(responseBody: JSONObject): SubsonicResult.Failure {
        val errorObj = responseBody.optJSONObject("error")
        val code = SubsonicErrorCode.fromCode(errorObj?.optInt("code") ?: 0)
        val message = errorObj?.optString("message")?.takeIf { it.isNotEmpty() } ?: "Unknown API error"
        return SubsonicResult.Failure(SubsonicError(code, message))
    }

    private fun genericFailure(message: String): SubsonicResult.Failure =
        SubsonicResult.Failure(SubsonicError(SubsonicErrorCode.GENERIC_ERROR, message))
}
