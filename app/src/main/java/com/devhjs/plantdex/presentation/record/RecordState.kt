package com.devhjs.plantdex.presentation.record

import androidx.compose.runtime.Stable
import com.devhjs.plantdex.domain.model.DexStats

@Stable
data class RecordState(
    val stats: DexStats = DexStats(),
)
