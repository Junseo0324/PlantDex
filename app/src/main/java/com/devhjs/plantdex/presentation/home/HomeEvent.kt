package com.devhjs.plantdex.presentation.home

sealed interface HomeEvent {
    data object NavigateToCamera : HomeEvent
    data object NavigateToCollection : HomeEvent
    data class NavigateToDetail(val entryId: Long) : HomeEvent
}
