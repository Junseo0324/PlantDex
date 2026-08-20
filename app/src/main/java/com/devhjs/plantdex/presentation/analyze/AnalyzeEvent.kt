package com.devhjs.plantdex.presentation.analyze

sealed interface AnalyzeEvent {
    data class Registered(val entryId: Long) : AnalyzeEvent
    data object Retake : AnalyzeEvent
}
