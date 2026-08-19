package com.devhjs.plantdex.domain.model

/**
 * 분석기가 내놓는 결과
 * 분석기가 발견일을 임의로 만들거나 범위 밖 난이도를 앱에 흘려넣을 수 없게 함
 */
data class PlantAnalysis(
    val name: String,
    val englishName: String,
    val description: String,
    val origin: String,
    val watering: String,
    val sunlight: Sunlight,
    val rawDifficulty: Int,
    val category: PlantCategory = PlantCategory.OTHER,
)
