package com.devhjs.plantdex.domain.datasource

import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.PlantAnalysis
import com.devhjs.plantdex.domain.model.PlantPhoto

/**
 * 사진에서 식물 정보를 얻는 포트. 어떤 AI 프로바이더를 쓰는지는 data 계층의 구현체가 결정한다.
 */
interface PlantAnalyzer {
    suspend fun analyze(photo: PlantPhoto): Result<PlantAnalysis, AnalysisError>
}
