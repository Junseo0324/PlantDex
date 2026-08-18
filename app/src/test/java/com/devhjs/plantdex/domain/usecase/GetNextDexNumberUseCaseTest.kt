package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.repository.DexRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetNextDexNumberUseCaseTest {

    private val repository = mockk<DexRepository>()
    private val subject = GetNextDexNumberUseCase(repository)

    @Test
    fun `저장소가 알려준 번호를 그대로 돌려준다`() = runTest {
        coEvery { repository.nextDexNumber() } returns 5

        assertEquals(5, subject())
    }

    @Test
    fun `빈 도감이면 1 이다`() = runTest {
        coEvery { repository.nextDexNumber() } returns 1

        assertEquals(1, subject())
    }

    @Test
    fun `등록하지 않고 번호만 읽는다`() = runTest {
        coEvery { repository.nextDexNumber() } returns 5

        subject()

        coVerify(exactly = 0) { repository.register(any(), any()) }
    }
}
