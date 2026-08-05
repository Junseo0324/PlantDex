package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.core.util.isSameMonthAs
import com.devhjs.plantdex.domain.model.DexSummary
import com.devhjs.plantdex.domain.repository.DexRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Clock

class ObserveDexSummaryUseCase @Inject constructor(
    private val dexRepository: DexRepository,
    private val clock: Clock,
) {
    operator fun invoke(recentLimit: Int): Flow<DexSummary> =
        dexRepository.observeAll().map { entries ->
            val now = clock.now()
            DexSummary(
                total = entries.size,
                thisMonth = entries.count { it.plant.discoveredAt.isSameMonthAs(now) },
                recent = entries.take(recentLimit),
            )
        }
}
