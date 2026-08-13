package com.devhjs.plantdex.presentation.detail

import androidx.compose.runtime.Stable
import com.devhjs.plantdex.domain.model.DexEntry

@Stable
data class DetailState(
    val entry: DexEntry? = null,
    val isMemoEditorOpen: Boolean = false,
    val memoDraft: String = "",
)
