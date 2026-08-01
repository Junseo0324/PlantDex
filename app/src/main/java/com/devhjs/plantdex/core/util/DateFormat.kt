package com.devhjs.plantdex.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Instant

/**
 * SimpleDateFormat 은 thread-safe 하지 않으므로 호출마다 새로 만든다.
 */
fun Instant.toKoreanDate(): String =
    SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA).format(Date(toEpochMilliseconds()))
