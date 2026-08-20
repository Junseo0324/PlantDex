package com.devhjs.plantdex.presentation.detail

sealed interface DetailEvent {
    data object NavigateBack : DetailEvent
}
