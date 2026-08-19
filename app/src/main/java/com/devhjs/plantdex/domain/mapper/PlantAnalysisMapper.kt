package com.devhjs.plantdex.domain.mapper

import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.PlantAnalysis
import kotlin.time.Instant

const val DIFFICULTY_MIN = 1
const val DIFFICULTY_MAX = 5

fun PlantAnalysis.toPlant(discoveredAt: Instant): Plant = Plant(
    name = name.trim(),
    englishName = englishName.trim(),
    description = description.trim(),
    origin = origin.trim(),
    watering = watering.trim(),
    sunlight = sunlight,
    difficulty = rawDifficulty.coerceIn(DIFFICULTY_MIN, DIFFICULTY_MAX),
    discoveredAt = discoveredAt,
    category = category,
)
