package com.devhjs.plantdex.domain.model

/**
 * 홈에서 보여주는 도감 요약
 */
data class DexSummary(
    val total: Int = 0,
    val thisMonth: Int = 0,
    val recent: List<DexEntry> = emptyList(),
)
