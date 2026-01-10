package com.vsulimov.libsubsonic.parser

import com.vsulimov.libsubsonic.data.Constants.DEFAULT_ERROR_CODE
import com.vsulimov.libsubsonic.data.result.SubsonicResult
import com.vsulimov.libsubsonic.data.result.error.SubsonicError
import org.json.JSONException
import org.json.JSONObject

/**
 * This class is responsible for parsing raw JSON strings into typed [SubsonicResult] objects.
 *
 * It handles the standard Subsonic JSON envelope, checks for API-level errors
 * (status="failed"), and extracts the relevant data payload.
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
                        code = DEFAULT_ERROR_CODE,
                        message = "Empty response received"
                    )
                )
            }

            val root = JSONObject(jsonString)

            if (!root.has("subsonic-response")) {
                return SubsonicResult.Failure(
                    SubsonicError(
                        code = DEFAULT_ERROR_CODE,
                        message = "Invalid JSON: Missing 'subsonic-response' root"
                    )
                )
            }

            val body = root.getJSONObject("subsonic-response")
            val status = body.optString("status")

            // Check for API Logic Error (e.g. Code 40: Wrong Password)
            if ("failed".equals(status, ignoreCase = true)) {
                return extractApiError(body)
            }

            // Success: Delegate parsing of the inner data to the caller
            val data = parser(body)
            SubsonicResult.Success(data)
        } catch (e: JSONException) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = DEFAULT_ERROR_CODE,
                    message = "JSON Parsing error: ${e.message}"
                )
            )
        } catch (e: Exception) {
            SubsonicResult.Failure(
                SubsonicError(
                    code = DEFAULT_ERROR_CODE,
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
        val code = errorObj?.optInt("code") ?: DEFAULT_ERROR_CODE
        val message = errorObj?.optString("message") ?: "Unknown API error"
        return SubsonicResult.Failure(SubsonicError(code, message))
    }
}
