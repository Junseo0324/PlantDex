package com.devhjs.plantdex.domain.model

import kotlin.time.Instant

/**
 * 검증을 통과한 식물 정보
 */
data class Plant(
    val name: String,
    val englishName: String,
    val description: String,
    val origin: String,
    val watering: String,
    val sunlight: Sunlight,
    val difficulty: Int,
    val discoveredAt: Instant,
    val category: PlantCategory = PlantCategory.OTHER,
)
