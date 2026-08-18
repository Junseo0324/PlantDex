package com.devhjs.plantdex.presentation.analyze

sealed interface AnalyzeAction {
    /** 실패 후 재시도. 최초 분석은 ViewModel 이 init 에서 시작한다. */
    data object Analyze : AnalyzeAction
    data object Register : AnalyzeAction

    data object Retake : AnalyzeAction
}
