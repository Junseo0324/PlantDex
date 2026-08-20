package com.devhjs.plantdex.presentation.collection

sealed interface CollectionEvent {
    data class NavigateToDetail(val entryId: Long) : CollectionEvent
}
