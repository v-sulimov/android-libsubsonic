package com.vsulimov.libsubsonic.auth

/**
 * Encodes a password using the Subsonic hex-encoding scheme.
 *
 * The encoded form is the string `"enc:"` followed by the lowercase hex
 * representation of the password's UTF-8 bytes. This is used to avoid sending
 * the password in clear text as a URL parameter.
 *
 * @param password The clear-text password to encode.
 * @return The hex-encoded password prefixed with `"enc:"`.
 */
internal fun encodePassword(password: String): String = "enc:" + password.toByteArray(Charsets.UTF_8).toHexString()
