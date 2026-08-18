package com.devhjs.plantdex.presentation.analyze

import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.Plant

sealed interface AnalyzeState {
    data object Loading : AnalyzeState
    data class Success(val plant: Plant, val nextDexNumber: Int) : AnalyzeState

    data class Error(val error: AnalysisError) : AnalyzeState
}
