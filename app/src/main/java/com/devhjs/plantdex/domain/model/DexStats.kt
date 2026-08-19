package com.devhjs.plantdex.domain.model

/**
 * 기록 화면이 보여주는 발견 통계
 */
data class DexStats(
    val year: Int = 0,
    val totalCount: Int = 0,
    val streakDays: Int = 0,
    val longestStreakDays: Int = 0,
    /** 최근 6개월, 과거 → 현재 순. 발견이 없는 달도 0 으로 들어간다. */
    val monthly: List<MonthlyCount> = emptyList(),
    /** 개수 내림차순. 0 인 분류는 빠진다. */
    val byCategory: List<CategoryCount> = emptyList(),
)

data class MonthlyCount(val month: Int, val count: Int)

data class CategoryCount(val category: PlantCategory, val count: Int)
