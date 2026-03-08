package com.vsulimov.libsubsonic.parser.bookmark

import com.vsulimov.libsubsonic.data.response.bookmark.Bookmark
import com.vsulimov.libsubsonic.data.response.bookmark.BookmarksResponse
import com.vsulimov.libsubsonic.parser.browsing.GetSongParser
import com.vsulimov.libsubsonic.parser.parseEnvelope
import com.vsulimov.libsubsonic.parser.parseList
import org.json.JSONObject

/**
 * Parses the `getBookmarks` response payload.
 */
internal object GetBookmarksParser {

    /**
     * Parses the "subsonic-response" object into a [BookmarksResponse].
     *
     * @param json The root "subsonic-response" JSONObject.
     * @return The parsed [BookmarksResponse].
     */
    fun parse(json: JSONObject): BookmarksResponse {
        val containerObj = json.optJSONObject("bookmarks")
        val bookmarks = containerObj?.parseList("bookmark") { bookmarkJson ->
            Bookmark(
                position = bookmarkJson.optLong("position"),
                username = bookmarkJson.optString("username"),
                comment = bookmarkJson.optString("comment").ifEmpty { null },
                created = bookmarkJson.optString("created"),
                changed = bookmarkJson.optString("changed"),
                entry = GetSongParser.parseSong(bookmarkJson.getJSONObject("entry"))
            )
        } ?: emptyList()

        val (status, apiVersion, serverType, serverVersion, isOpenSubsonic) = json.parseEnvelope()
        return BookmarksResponse(
            status = status,
            apiVersion = apiVersion,
            serverType = serverType,
            serverVersion = serverVersion,
            isOpenSubsonic = isOpenSubsonic,
            bookmarks = bookmarks
        )
    }
}
