package com.devhjs.plantdex.presentation.detail

sealed interface DetailAction {
    /** 화면에 들어올 때 Root 가 보낸다. */
    data class Load(val entryId: Long) : DetailAction

    data object ToggleFavorite : DetailAction
    data object OpenMemoEditor : DetailAction
    data object DismissMemoEditor : DetailAction
    data class MemoDraftChanged(val text: String) : DetailAction
    data object SaveMemo : DetailAction

    data object Back : DetailAction
}
