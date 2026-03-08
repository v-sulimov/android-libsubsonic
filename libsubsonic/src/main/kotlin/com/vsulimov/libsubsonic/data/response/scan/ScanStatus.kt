package com.vsulimov.libsubsonic.data.response.scan

/**
 * Represents the current media library scan status.
 *
 * @property scanning Whether a scan is currently in progress.
 * @property count The number of media files scanned so far, if available.
 */
data class ScanStatus(
    val scanning: Boolean,
    val count: Long?
)
