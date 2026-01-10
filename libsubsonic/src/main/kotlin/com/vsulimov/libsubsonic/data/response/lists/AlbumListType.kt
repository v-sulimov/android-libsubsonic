package com.vsulimov.libsubsonic.data.response.lists

/**
 * Represents the list type used by the `getAlbumList2` endpoint to determine
 * which set of albums to return.
 *
 * @property value The string value sent to the Subsonic server as the `type` parameter.
 */
enum class AlbumListType(val value: String) {

    /** Returns albums in random order. */
    RANDOM("random"),

    /** Returns the most recently added albums. */
    NEWEST("newest"),

    /** Returns the most frequently played albums. */
    FREQUENT("frequent"),

    /** Returns the most recently played albums. */
    RECENT("recent"),

    /** Returns albums that have been starred by the current user. */
    STARRED("starred"),

    /** Returns albums sorted alphabetically by album name. */
    ALPHABETICAL_BY_NAME("alphabeticalByName"),

    /** Returns albums sorted alphabetically by artist name. */
    ALPHABETICAL_BY_ARTIST("alphabeticalByArtist"),

    /** Returns albums filtered by release year range. Requires [fromYear] and [toYear] parameters. */
    BY_YEAR("byYear"),

    /** Returns albums filtered by genre. Requires the [genre] parameter. */
    BY_GENRE("byGenre")
}
