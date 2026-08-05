package com.devhjs.plantdex.presentation.home

import androidx.compose.runtime.Stable
import com.devhjs.plantdex.domain.model.DexEntry

@Stable
data class HomeState(
    val discoveredCount: Int = 0,
    val thisMonthCount: Int = 0,
    val recent: List<DexEntry> = emptyList(),
)

const val RECENT_LIMIT = 3
