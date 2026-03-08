package com.vsulimov.libsubsonic.data.response.user

/**
 * Represents the maximum bit rate (in Kbps) allowed for a user's audio streams.
 *
 * Audio streams of higher bit rates are automatically downsampled to the selected value.
 *
 * @property value The numeric bit rate value sent to the server.
 */
enum class MaxBitRate(val value: Int) {

    /** No bit rate limit; the server streams at the original file quality. */
    NO_LIMIT(0),

    /** Limits audio streams to 32 Kbps. */
    KBPS_32(32),

    /** Limits audio streams to 40 Kbps. */
    KBPS_40(40),

    /** Limits audio streams to 48 Kbps. */
    KBPS_48(48),

    /** Limits audio streams to 56 Kbps. */
    KBPS_56(56),

    /** Limits audio streams to 64 Kbps. */
    KBPS_64(64),

    /** Limits audio streams to 80 Kbps. */
    KBPS_80(80),

    /** Limits audio streams to 96 Kbps. */
    KBPS_96(96),

    /** Limits audio streams to 112 Kbps. */
    KBPS_112(112),

    /** Limits audio streams to 128 Kbps. */
    KBPS_128(128),

    /** Limits audio streams to 160 Kbps. */
    KBPS_160(160),

    /** Limits audio streams to 192 Kbps. */
    KBPS_192(192),

    /** Limits audio streams to 224 Kbps. */
    KBPS_224(224),

    /** Limits audio streams to 256 Kbps. */
    KBPS_256(256),

    /** Limits audio streams to 320 Kbps. */
    KBPS_320(320)
}
