package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.repository.DexRepository
import javax.inject.Inject


class GetNextDexNumberUseCase @Inject constructor(
    private val dexRepository: DexRepository,
) {
    suspend operator fun invoke(): Int = dexRepository.nextDexNumber()
}
