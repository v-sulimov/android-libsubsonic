package com.vsulimov.libsubsonic.data.result.error

import kotlin.test.Test
import kotlin.test.assertEquals

class SubsonicErrorCodeTest {

    @Test
    fun `fromCode returns correct enum for all known codes`() {
        assertEquals(SubsonicErrorCode.GENERIC_ERROR, SubsonicErrorCode.fromCode(0))
        assertEquals(SubsonicErrorCode.MISSING_PARAMETER, SubsonicErrorCode.fromCode(10))
        assertEquals(SubsonicErrorCode.INCOMPATIBLE_VERSION, SubsonicErrorCode.fromCode(20))
        assertEquals(SubsonicErrorCode.WRONG_CREDENTIALS_LEGACY, SubsonicErrorCode.fromCode(30))
        assertEquals(SubsonicErrorCode.WRONG_CREDENTIALS, SubsonicErrorCode.fromCode(40))
        assertEquals(SubsonicErrorCode.TOKEN_AUTH_NOT_SUPPORTED, SubsonicErrorCode.fromCode(41))
        assertEquals(SubsonicErrorCode.NOT_AUTHORIZED, SubsonicErrorCode.fromCode(50))
        assertEquals(SubsonicErrorCode.TRIAL_EXPIRED, SubsonicErrorCode.fromCode(60))
        assertEquals(SubsonicErrorCode.DATA_NOT_FOUND, SubsonicErrorCode.fromCode(70))
    }

    @Test
    fun `fromCode returns GENERIC_ERROR for unknown code`() {
        assertEquals(SubsonicErrorCode.GENERIC_ERROR, SubsonicErrorCode.fromCode(99))
        assertEquals(SubsonicErrorCode.GENERIC_ERROR, SubsonicErrorCode.fromCode(-1))
    }

    @Test
    fun `enum entries have correct numeric codes`() {
        assertEquals(0, SubsonicErrorCode.GENERIC_ERROR.code)
        assertEquals(10, SubsonicErrorCode.MISSING_PARAMETER.code)
        assertEquals(20, SubsonicErrorCode.INCOMPATIBLE_VERSION.code)
        assertEquals(30, SubsonicErrorCode.WRONG_CREDENTIALS_LEGACY.code)
        assertEquals(40, SubsonicErrorCode.WRONG_CREDENTIALS.code)
        assertEquals(41, SubsonicErrorCode.TOKEN_AUTH_NOT_SUPPORTED.code)
        assertEquals(50, SubsonicErrorCode.NOT_AUTHORIZED.code)
        assertEquals(60, SubsonicErrorCode.TRIAL_EXPIRED.code)
        assertEquals(70, SubsonicErrorCode.DATA_NOT_FOUND.code)
    }
}
