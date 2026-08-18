package com.devhjs.plantdex.presentation.analyze

import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.Plant

/**
 * 촬영을 마치고 들어오는 화면이라 시작부터 [Loading] 이다. 대기 상태가 없다.
 */
sealed interface AnalyzeState {
    data object Loading : AnalyzeState
    data class Success(val plant: Plant) : AnalyzeState

    /** 문자열이 아니라 도메인 에러를 그대로 들고 있어 ViewModel 을 Context 없이 테스트할 수 있다. */
    data class Error(val error: AnalysisError) : AnalyzeState
}
