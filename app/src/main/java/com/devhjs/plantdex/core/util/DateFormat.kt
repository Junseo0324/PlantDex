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

/**
 * 기기 시간대 기준 epoch day. 연속 발견 일수는 "하루 빼기"가 필요해서 연·월·일 대신 이 값으로 센다.
 */
fun Instant.toLocalEpochDay(): Long {
    val millis = toEpochMilliseconds()
    val calendar = toCalendar()
    val offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)
    return Math.floorDiv(millis + offset, MILLIS_PER_DAY)
}

fun Instant.year(): Int = toCalendar().get(Calendar.YEAR)

internal fun Instant.toCalendar(): Calendar =
    Calendar.getInstance().apply { timeInMillis = toEpochMilliseconds() }

private const val MILLIS_PER_DAY = 24L * 60 * 60 * 1000
