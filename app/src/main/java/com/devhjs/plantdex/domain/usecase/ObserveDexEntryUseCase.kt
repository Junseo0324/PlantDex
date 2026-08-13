package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.repository.DexRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDexEntryUseCase @Inject constructor(
    private val dexRepository: DexRepository,
) {
    operator fun invoke(id: Long): Flow<DexEntry?> = dexRepository.observe(id)
}
