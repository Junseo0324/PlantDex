package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.repository.DexRepository
import javax.inject.Inject

/**
 * 토글이 아니라 목표값을 받는다. 현재값은 화면이 이미 알고 있고, 다시 읽으면 읽기-쓰기 경합이 생긴다.
 */
class SetFavoriteUseCase @Inject constructor(
    private val dexRepository: DexRepository,
) {
    suspend operator fun invoke(id: Long, favorite: Boolean) =
        dexRepository.setFavorite(id, favorite)
}
