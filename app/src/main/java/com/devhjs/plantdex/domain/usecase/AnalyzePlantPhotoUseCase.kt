package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.core.util.map
import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.datasource.PlantAnalyzer
import com.devhjs.plantdex.domain.mapper.toPlant
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.PlantPhoto
import javax.inject.Inject
import kotlin.time.Clock

class AnalyzePlantPhotoUseCase @Inject constructor(
    private val analyzer: PlantAnalyzer,
    private val clock: Clock,
) {
    suspend operator fun invoke(photo: PlantPhoto): Result<Plant, AnalysisError> =
        analyzer.analyze(photo).map { it.toPlant(discoveredAt = clock.now()) }
}
