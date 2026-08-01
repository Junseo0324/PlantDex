package com.devhjs.plantdex.data.datasource

import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.domain.datasource.PlantAnalyzer
import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.PlantAnalysis
import com.devhjs.plantdex.domain.model.PlantPhoto
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * AI 프로바이더가 정해지기 전까지 쓰는 가짜 분석기.
 *
 * 다른 결과 : MockPlants 항목 변경
 * 에러 화면 : Result.Error(AnalysisError.NotAPlant)
 */
class MockPlantAnalyzer @Inject constructor() : PlantAnalyzer {

    override suspend fun analyze(photo: PlantPhoto): Result<PlantAnalysis, AnalysisError> {
        delay(LATENCY)
        return Result.Success(MockPlants.Monstera)
    }

    private companion object {
        /** 로딩 상태가 눈에 보이도록 하는 지연. runTest 에서는 가상 시간이라 즉시 끝난다. */
        val LATENCY = 1_500.milliseconds
    }
}
