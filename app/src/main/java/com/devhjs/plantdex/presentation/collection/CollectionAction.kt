package com.devhjs.plantdex.presentation.collection

sealed interface CollectionAction {
    data class QueryChanged(val query: String) : CollectionAction
    data class FilterChanged(val filter: CollectionFilter) : CollectionAction

    data class OpenDetail(val entryId: Long) : CollectionAction
}
