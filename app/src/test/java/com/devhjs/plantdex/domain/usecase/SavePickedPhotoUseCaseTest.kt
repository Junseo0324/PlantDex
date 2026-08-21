package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.datasource.PhotoStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SavePickedPhotoUseCaseTest {

    private companion object {
        const val CONTENT_URI = "content://media/picker/0/1"
        const val FILE_URI = "file:///data/photos/1-abc.jpg"
    }

    private val photoStore = mockk<PhotoStore>()
    private val subject = SavePickedPhotoUseCase(photoStore)

    @Test
    fun `복사한 위치를 그대로 돌려준다`() = runTest {
        coEvery { photoStore.save(CONTENT_URI) } returns FILE_URI

        assertEquals(FILE_URI, subject(CONTENT_URI))
    }

    @Test
    fun `복사에 실패하면 null 이다`() = runTest {
        coEvery { photoStore.save(any()) } returns null

        assertNull(subject(CONTENT_URI))
    }

    @Test
    fun `고른 위치를 저장소에 그대로 넘긴다`() = runTest {
        coEvery { photoStore.save(any()) } returns FILE_URI

        subject(CONTENT_URI)

        coVerify(exactly = 1) { photoStore.save(CONTENT_URI) }
    }
}
