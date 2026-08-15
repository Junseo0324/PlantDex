package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.DexCollection
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.repository.DexRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveDexCollectionUseCase @Inject constructor(
    private val dexRepository: DexRepository,
) {
    operator fun invoke(query: String, favoritesOnly: Boolean): Flow<DexCollection> =
        dexRepository.observeAll().map { all ->
            val keyword = query.trim()
            DexCollection(
                entries = all.filter { it.matches(keyword) && (!favoritesOnly || it.isFavorite) },
                totalCount = all.size,
                lastDiscoveredAt = all.maxOfOrNull { it.plant.discoveredAt },
            )
        }

    private fun DexEntry.matches(keyword: String): Boolean =
        keyword.isEmpty() ||
            plant.name.contains(keyword, ignoreCase = true) ||
            plant.englishName.contains(keyword, ignoreCase = true)
}
