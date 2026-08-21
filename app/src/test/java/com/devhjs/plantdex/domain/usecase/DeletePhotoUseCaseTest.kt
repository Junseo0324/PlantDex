package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.datasource.PhotoStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeletePhotoUseCaseTest {

    private companion object {
        const val FILE_URI = "file:///data/photos/1-abc.jpg"
    }

    private val photoStore = mockk<PhotoStore> {
        coEvery { delete(any()) } returns Unit
    }
    private val subject = DeletePhotoUseCase(photoStore)

    @Test
    fun `사진을 지운다`() = runTest {
        subject(FILE_URI)

        coVerify(exactly = 1) { photoStore.delete(FILE_URI) }
    }

    /** 등록 전에 사진이 없을 수 있어 null 이 그대로 들어온다. */
    @Test
    fun `위치가 없으면 저장소를 건드리지 않는다`() = runTest {
        subject(null)

        coVerify(exactly = 0) { photoStore.delete(any()) }
    }
}
