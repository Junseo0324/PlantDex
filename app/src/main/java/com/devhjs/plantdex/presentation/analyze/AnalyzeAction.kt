package com.devhjs.plantdex.presentation.analyze

sealed interface AnalyzeAction {
    /** 화면에 들어올 때 Root 가 촬영한 사진을 넘긴다. */
    data class Start(val photoUri: String?) : AnalyzeAction

    /** 재시도. 사진은 ViewModel 이 들고 있는 걸 그대로 쓴다. */
    data object Retry : AnalyzeAction

    data object Register : AnalyzeAction

    data object Retake : AnalyzeAction
}
