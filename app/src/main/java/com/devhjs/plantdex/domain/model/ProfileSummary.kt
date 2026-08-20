package com.devhjs.plantdex.domain.model

/**
 * 내정보 화면이 필요로 하는 것 전부
 */
data class ProfileSummary(
    val user: UserProfile? = null,
    val level: Int = 1,
    val levelTitle: PlantLevel = PlantLevel.SEEDLING,
    val discoveredCount: Int = 0,
    val favoriteCount: Int = 0,
    val memoCount: Int = 0,
    val nextLevelTarget: Int = 0,
)
