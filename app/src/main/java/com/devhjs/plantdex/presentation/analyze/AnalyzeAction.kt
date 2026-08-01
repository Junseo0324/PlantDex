package com.devhjs.plantdex.presentation.analyze

sealed interface AnalyzeAction {
    data object Analyze : AnalyzeAction
    data object Reset : AnalyzeAction
}
