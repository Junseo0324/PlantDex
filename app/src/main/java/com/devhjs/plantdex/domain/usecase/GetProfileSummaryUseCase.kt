package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.PlantLevel
import com.devhjs.plantdex.domain.model.ProfileSummary
import com.devhjs.plantdex.domain.repository.DexRepository
import com.devhjs.plantdex.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/** 이만큼 발견할 때마다 레벨이 하나 오른다. */
const val PLANTS_PER_LEVEL = 10

class GetProfileSummaryUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dexRepository: DexRepository,
) {
    operator fun invoke(): Flow<ProfileSummary> =
        combine(userRepository.observeProfile(), dexRepository.observeAll()) { user, entries ->
            val level = entries.size / PLANTS_PER_LEVEL + 1
            ProfileSummary(
                user = user,
                level = level,
                levelTitle = titleOf(level),
                discoveredCount = entries.size,
                favoriteCount = entries.count(DexEntry::isFavorite),
                memoCount = entries.count { !it.memo.isNullOrBlank() },
                nextLevelTarget = level * PLANTS_PER_LEVEL,
            )
        }

    private fun titleOf(level: Int): PlantLevel = when (level) {
        1 -> PlantLevel.SEEDLING
        in 2..4 -> PlantLevel.OBSERVER
        in 5..7 -> PlantLevel.COLLECTOR
        else -> PlantLevel.DOCTOR
    }
}
