package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.datasource.PhotoStore
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(
    private val photoStore: PhotoStore,
) {
    suspend operator fun invoke(photoUri: String) = photoStore.delete(photoUri)
}
