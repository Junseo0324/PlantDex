package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.repository.DexRepository
import javax.inject.Inject

class SaveMemoUseCase @Inject constructor(
    private val dexRepository: DexRepository,
) {
    suspend operator fun invoke(id: Long, memo: String) =
        dexRepository.setMemo(id, memo.trim())
}
