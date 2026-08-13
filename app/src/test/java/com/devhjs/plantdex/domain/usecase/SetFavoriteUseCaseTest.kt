package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.repository.DexRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SetFavoriteUseCaseTest {

    private val repository = mockk<DexRepository> {
        coEvery { setFavorite(any(), any()) } returns Unit
    }

    private val subject = SetFavoriteUseCase(repository)

    @Test
    fun `켜는 요청이 그대로 전달된다`() = runTest {
        subject(id = 1L, favorite = true)

        coVerify(exactly = 1) { repository.setFavorite(1L, true) }
    }

    @Test
    fun `끄는 요청이 그대로 전달된다`() = runTest {
        subject(id = 2L, favorite = false)

        coVerify(exactly = 1) { repository.setFavorite(2L, false) }
    }

    @Test
    fun `현재값을 다시 읽지 않는다`() = runTest {
        subject(id = 3L, favorite = true)

        coVerify(exactly = 0) { repository.observe(any()) }
        coVerify(exactly = 0) { repository.observeAll() }
    }
}
