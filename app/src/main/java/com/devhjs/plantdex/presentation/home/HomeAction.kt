package com.devhjs.plantdex.presentation.home

sealed interface HomeAction {
    data object Discover : HomeAction
    data object SeeAllCollection : HomeAction
    data class OpenDetail(val entryId: Long) : HomeAction
}
