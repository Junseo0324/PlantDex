package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.datasource.PhotoStore
import javax.inject.Inject

class SavePhotoUseCase @Inject constructor(
    private val photoStore: PhotoStore,
) {
    /** 복사한 사진의 위치. 실패하면 null. */
    suspend operator fun invoke(sourceUri: String): String? = photoStore.save(sourceUri)
}
