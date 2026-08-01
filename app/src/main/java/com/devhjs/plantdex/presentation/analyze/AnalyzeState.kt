package com.devhjs.plantdex.presentation.analyze

import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.Plant

sealed interface AnalyzeState {
    data object Idle : AnalyzeState
    data object Loading : AnalyzeState
    data class Success(val plant: Plant) : AnalyzeState

    /** 문자열이 아니라 도메인 에러를 그대로 들고 있어 ViewModel 을 Context 없이 테스트할 수 있다. */
    data class Error(val error: AnalysisError) : AnalyzeState
}
