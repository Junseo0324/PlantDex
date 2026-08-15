package com.devhjs.plantdex.core.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Instant

/**
 * SimpleDateFormat 은 thread-safe 하지 않으므로 호출마다 새로 만든다.
 */
fun Instant.toKoreanDate(): String =
    SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA).format(Date(toEpochMilliseconds()))

/** 2026.07.28 형태. 도감 헤더처럼 좁은 자리에 쓴다. */
fun Instant.toDotDate(): String =
    SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(Date(toEpochMilliseconds()))

fun Instant.isSameMonthAs(other: Instant): Boolean {
    val a = toCalendar()
    val b = other.toCalendar()
    return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
        a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
}

private fun Instant.toCalendar(): Calendar =
    Calendar.getInstance().apply { timeInMillis = toEpochMilliseconds() }
