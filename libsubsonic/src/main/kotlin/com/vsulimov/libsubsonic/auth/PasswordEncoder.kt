package com.vsulimov.libsubsonic.auth

/** Reusable lowercase hex formatter, allocated once to avoid per-call overhead. */
private val hexFormat = HexFormat { upperCase = false }

/**
 * Encodes a password using the Subsonic hex-encoding scheme.
 *
 * The encoded form is the string `"enc:"` followed by the lowercase hex
 * representation of the password's UTF-8 bytes. This prevents sending
 * the password in clear text as an API parameter.
 *
 * @param password The clear-text password to encode.
 * @return The hex-encoded password prefixed with `"enc:"`.
 */
internal fun encodePassword(password: String): String {
    return "enc:" + password.toByteArray(Charsets.UTF_8).toHexString(hexFormat)
}
