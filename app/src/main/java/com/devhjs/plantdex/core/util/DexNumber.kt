package com.devhjs.plantdex.core.util

const val DEX_NUMBER_DIGITS = 3

/**
 * 도감 번호를 화면 표기용 문자열로 바꾼다. 1 -> "No.001", 23 -> "No.023"
 *
 */
fun Int.toDexLabel(): String = "No." + toString().padStart(DEX_NUMBER_DIGITS, '0')
