package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.PlantLevel
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.model.UserProfile
import com.devhjs.plantdex.domain.repository.DexRepository
import com.devhjs.plantdex.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Instant

class GetProfileSummaryUseCaseTest {

    private val joinedAt = Instant.fromEpochMilliseconds(1_772_409_600_000)
    private val user = UserProfile(name = "홍길동", joinedAt = joinedAt)

    private val userRepository = mockk<UserRepository> {
        every { observeProfile() } returns flowOf(user)
    }

    private val entries = MutableStateFlow<List<DexEntry>>(emptyList())
    private val dexRepository = mockk<DexRepository> { every { observeAll() } returns entries }

    private val subject = GetProfileSummaryUseCase(userRepository, dexRepository)

    private val plant = Plant(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "열대 관엽식물",
        origin = "멕시코 남부",
        watering = "2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        difficulty = 2,
        discoveredAt = joinedAt,
    )

    private fun given(count: Int, favorites: Int = 0, memos: Int = 0) {
        entries.value = (1..count).map { number ->
            DexEntry(
                id = number.toLong(),
                dexNumber = number,
                plant = plant,
                isFavorite = number <= favorites,
                memo = if (number <= memos) "메모" else null,
            )
        }
    }

    private suspend fun summary() = subject().first()

    @Test
    fun `도감이 비면 Lv 1 이고 다음 목표는 10 이다`() = runTest {
        val summary = summary()

        assertEquals(0, summary.discoveredCount)
        assertEquals(1, summary.level)
        assertEquals(PlantLevel.SEEDLING, summary.levelTitle)
        assertEquals(10, summary.nextLevelTarget)
    }

    @Test
    fun `23종이면 Lv 3 이고 다음 목표는 30 이다`() = runTest {
        given(23)

        val summary = summary()

        assertEquals(3, summary.level)
        assertEquals(30, summary.nextLevelTarget)
    }

    @Test
    fun `정확히 30종이면 Lv 4 로 올라간다`() = runTest {
        given(30)

        val summary = summary()

        assertEquals(4, summary.level)
        assertEquals(40, summary.nextLevelTarget)
    }

    @Test
    fun `레벨 구간마다 칭호가 붙는다`() = runTest {
        given(0)
        assertEquals(PlantLevel.SEEDLING, summary().levelTitle)

        given(10)
        assertEquals(PlantLevel.OBSERVER, summary().levelTitle)

        given(40)
        assertEquals(PlantLevel.COLLECTOR, summary().levelTitle)

        given(70)
        assertEquals(PlantLevel.DOCTOR, summary().levelTitle)
    }

    @Test
    fun `즐겨찾기와 메모 개수를 센다`() = runTest {
        given(count = 5, favorites = 2, memos = 3)

        val summary = summary()

        assertEquals(5, summary.discoveredCount)
        assertEquals(2, summary.favoriteCount)
        assertEquals(3, summary.memoCount)
    }

    @Test
    fun `빈 메모는 메모로 세지 않는다`() = runTest {
        entries.value = listOf(
            DexEntry(id = 1, dexNumber = 1, plant = plant, memo = "   "),
            DexEntry(id = 2, dexNumber = 2, plant = plant, memo = "잘 자란다"),
        )

        assertEquals(1, summary().memoCount)
    }

    @Test
    fun `사용자 정보가 그대로 실린다`() = runTest {
        assertEquals(user, summary().user)
    }

    @Test
    fun `도감이 바뀌면 요약도 다시 흐른다`() = runTest {
        given(1)
        assertEquals(1, summary().discoveredCount)

        given(2)
        assertEquals(2, summary().discoveredCount)
    }
}
