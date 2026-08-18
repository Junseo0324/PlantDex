package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.repository.DexRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Instant

class RegisterDexEntryUseCaseTest {

    private val plant = Plant(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "열대 관엽식물",
        origin = "멕시코 남부",
        watering = "2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        difficulty = 2,
        discoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000),
    )

    private val registered = DexEntry(id = 5L, dexNumber = 5, plant = plant)

    private val repository = mockk<DexRepository> {
        coEvery { register(any(), any()) } returns registered
    }
    private val subject = RegisterDexEntryUseCase(repository)

    @Test
    fun `등록된 항목을 그대로 돌려준다`() = runTest {
        assertEquals(registered, subject(plant))
    }

    @Test
    fun `사진 없이 등록하면 photoUri 는 null 로 전달된다`() = runTest {
        subject(plant)

        coVerify(exactly = 1) { repository.register(plant, null) }
    }

    @Test
    fun `사진 위치를 넘기면 그대로 전달된다`() = runTest {
        subject(plant, photoUri = "content://photo/1")

        coVerify(exactly = 1) { repository.register(plant, "content://photo/1") }
    }
}
