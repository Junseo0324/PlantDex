package com.devhjs.plantdex.presentation.collection

import androidx.compose.runtime.Stable
import com.devhjs.plantdex.domain.model.DexEntry
import kotlin.time.Instant


@Stable
data class CollectionState(
    val query: String = "",
    val filter: CollectionFilter = CollectionFilter.All,
    val entries: List<DexEntry> = emptyList(),
    val totalCount: Int = 0,
    val lastDiscoveredAt: Instant? = null,
)
