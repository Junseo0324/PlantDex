package com.devhjs.plantdex.domain.model

sealed interface AnalysisError {
    data object Network : AnalysisError
    data object Timeout : AnalysisError
    data object QuotaExceeded : AnalysisError

    /** 사진에서 식물을 찾지 못함 — 재시도가 아니라 재촬영이 필요하다. */
    data object NotAPlant : AnalysisError
    data object MalformedResponse : AnalysisError
    data class Unknown(val cause: Throwable? = null) : AnalysisError
}
