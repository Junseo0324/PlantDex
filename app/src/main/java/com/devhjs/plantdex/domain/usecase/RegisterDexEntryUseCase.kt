package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.repository.DexRepository
import javax.inject.Inject

class RegisterDexEntryUseCase @Inject constructor(
    private val dexRepository: DexRepository,
) {
    suspend operator fun invoke(plant: Plant, photoUri: String? = null): DexEntry =
        dexRepository.register(plant, photoUri)
}
