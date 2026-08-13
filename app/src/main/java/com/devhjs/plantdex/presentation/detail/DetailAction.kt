package com.devhjs.plantdex.presentation.detail

sealed interface DetailAction {
    data object ToggleFavorite : DetailAction
    data object OpenMemoEditor : DetailAction
    data object DismissMemoEditor : DetailAction
    data class MemoDraftChanged(val text: String) : DetailAction
    data object SaveMemo : DetailAction

    data object Back : DetailAction
}
