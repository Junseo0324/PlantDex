package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.repository.DexRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SaveMemoUseCaseTest {

    private val repository = mockk<DexRepository> {
        coEvery { setMemo(any(), any()) } returns Unit
    }

    private val subject = SaveMemoUseCase(repository)

    @Test
    fun `앞뒤 공백은 제거되고 저장된다`() = runTest {
        subject(id = 1L, memo = "  창가 화분 \n")

        coVerify(exactly = 1) { repository.setMemo(1L, "창가 화분") }
    }

    @Test
    fun `이미 정리된 문자열은 그대로 저장된다`() = runTest {
        subject(id = 2L, memo = "창가 화분")

        coVerify(exactly = 1) { repository.setMemo(2L, "창가 화분") }
    }

    @Test
    fun `공백만 있으면 빈 문자열이 된다`() = runTest {
        subject(id = 3L, memo = "   \n\t ")

        coVerify(exactly = 1) { repository.setMemo(3L, "") }
    }

    @Test
    fun `가운데 공백은 건드리지 않는다`() = runTest {
        subject(id = 4L, memo = "  창가  화분  ")

        coVerify(exactly = 1) { repository.setMemo(4L, "창가  화분") }
    }
}
