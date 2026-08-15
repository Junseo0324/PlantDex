package com.devhjs.plantdex.domain.model

import kotlin.time.Instant

data class DexCollection(
    val entries: List<DexEntry> = emptyList(),
    val totalCount: Int = 0,
    val lastDiscoveredAt: Instant? = null,
)
