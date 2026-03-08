package com.vsulimov.libsubsonic.parser

import com.vsulimov.libsubsonic.data.result.SubsonicResult
import com.vsulimov.libsubsonic.data.result.error.SubsonicError
import com.vsulimov.libsubsonic.data.result.error.SubsonicErrorCode
import org.json.JSONException
import org.json.JSONObject

/**
 * Parses raw JSON responses into typed [SubsonicResult] instances.
 *
 * This parser handles the standard "subsonic-response" envelope, detects API-level
 * failures (status="failed"), and delegates payload parsing to the provided lambda.
 */
internal class SubsonicResponseParser {

    /**
     * Parses a raw JSON response string.
     *
     * @param jsonString The raw response body from the server.
     * @param parser A lambda that transforms the successful JSON payload into type [T].
     * @return A [SubsonicResult] containing either the parsed data or an error.
     */
    fun <T> parse(jsonString: String, parser: (JSONObject) -> T): SubsonicResult<T> {
        return try {
            if (jsonString.isBlank()) {
                return SubsonicResult.Failure(
                    SubsonicError(
                        code = SubsonicErrorCode.GENERIC_ERROR,
                        message = "Empty response received"
                    )
                )
            }

            val root = JSONObject(jsonString)

            if (!root.has("subsonic-response")) {
                return SubsonicResult.Failure(
                    SubsonicError(
                        code = SubsonicErrorCode.GENERIC_ERROR,
                        message = "Invalid JSON: Missing 'subsonic-response' root"
                    )
                )
            }

            val body = root.getJSONObject("subsonic-response")
            val status = body.optString("status")

            if ("failed".equals(status, ignoreCase = true)) {
                return extractApiError(body)
            }

            val data = parser(body)
            SubsonicResult.Success(data)
        } catch (e: JSONException) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = SubsonicErrorCode.GENERIC_ERROR,
                    message = "JSON Parsing error: ${e.message}"
                )
            )
        } catch (e: Exception) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = SubsonicErrorCode.GENERIC_ERROR,
                    message = "Unknown parsing error: ${e.message}"
                )
            )
        }
    }

    /**
     * Extracts the structured error object from a failed response.
     *
     * @param responseBody The "subsonic-response" JSON object.
     * @return A [SubsonicResult.Failure] containing the error code and message.
     */
    private fun extractApiError(responseBody: JSONObject): SubsonicResult.Failure {
        val errorObj = responseBody.optJSONObject("error")
        val code = SubsonicErrorCode.fromCode(errorObj?.optInt("code") ?: 0)
        val message = errorObj?.optString("message")?.takeIf { it.isNotEmpty() } ?: "Unknown API error"
        return SubsonicResult.Failure(SubsonicError(code, message))
    }
}
