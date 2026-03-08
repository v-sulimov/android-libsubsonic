package com.vsulimov.libsubsonic.data.response.annotation

/**
 * Represents the rating value used by the `setRating` endpoint.
 *
 * @property value The integer value sent to the Subsonic server as the `rating` parameter.
 */
enum class Rating(val value: Int) {

    /** Removes the rating from the item. */
    REMOVE(0),

    /** Sets a rating of 1 star. */
    ONE(1),

    /** Sets a rating of 2 stars. */
    TWO(2),

    /** Sets a rating of 3 stars. */
    THREE(3),

    /** Sets a rating of 4 stars. */
    FOUR(4),

    /** Sets a rating of 5 stars. */
    FIVE(5)
}
