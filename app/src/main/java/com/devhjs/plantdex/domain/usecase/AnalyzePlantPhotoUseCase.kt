package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.core.util.map
import com.devhjs.plantdex.domain.datasource.PhotoLoader
import com.devhjs.plantdex.domain.datasource.PlantAnalyzer
import com.devhjs.plantdex.domain.mapper.toPlant
import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.Plant
import javax.inject.Inject
import kotlin.time.Clock

class AnalyzePlantPhotoUseCase @Inject constructor(
    private val photoLoader: PhotoLoader,
    private val analyzer: PlantAnalyzer,
    private val clock: Clock,
) {
    suspend operator fun invoke(photoUri: String): Result<Plant, AnalysisError> {
        val photo = photoLoader.load(photoUri)
            ?: return Result.Error(AnalysisError.PhotoUnavailable)

        return analyzer.analyze(photo).map { it.toPlant(discoveredAt = clock.now()) }
    }
}
