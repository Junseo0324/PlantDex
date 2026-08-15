package com.devhjs.plantdex.core.util

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone
import kotlin.time.Instant

/**
 * SimpleDateFormat 과 Calendar 는 기본 시간대를 따르므로 시간대를 고정하고 검증한다.
 */
class DateFormatTest {

    private val seoul = TimeZone.getTimeZone("Asia/Seoul")
    private lateinit var original: TimeZone

    @Before
    fun fixTimeZone() {
        original = TimeZone.getDefault()
        TimeZone.setDefault(seoul)
    }

    @After
    fun restoreTimeZone() {
        TimeZone.setDefault(original)
    }

    /** 시간대 경계에 걸리지 않도록 정오로 만든다. */
    private fun instantAt(year: Int, month: Int, day: Int): Instant {
        val calendar = Calendar.getInstance(seoul)
        calendar.clear()
        calendar.set(year, month - 1, day, 12, 0, 0)
        return Instant.fromEpochMilliseconds(calendar.timeInMillis)
    }

    @Test
    fun `toKoreanDate 는 0 을 채우지 않는다`() {
        assertEquals("2026년 8월 5일", instantAt(2026, 8, 5).toKoreanDate())
        assertEquals("2026년 12월 25일", instantAt(2026, 12, 25).toKoreanDate())
    }

    @Test
    fun `toDotDate 는 두 자리로 채운다`() {
        assertEquals("2026.08.05", instantAt(2026, 8, 5).toDotDate())
        assertEquals("2026.12.25", instantAt(2026, 12, 25).toDotDate())
    }

    @Test
    fun `같은 달의 다른 날은 같은 달이다`() {
        assertTrue(instantAt(2026, 8, 1).isSameMonthAs(instantAt(2026, 8, 31)))
    }

    @Test
    fun `다른 달이면 다르다`() {
        assertFalse(instantAt(2026, 8, 31).isSameMonthAs(instantAt(2026, 9, 1)))
    }

    @Test
    fun `해가 다르면 같은 달이어도 다르다`() {
        assertFalse(instantAt(2025, 8, 5).isSameMonthAs(instantAt(2026, 8, 5)))
    }
}
