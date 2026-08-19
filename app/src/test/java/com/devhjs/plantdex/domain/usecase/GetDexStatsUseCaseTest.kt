package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.CategoryCount
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.PlantCategory
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.repository.DexRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class GetDexStatsUseCaseTest {

    private val now = Instant.fromEpochMilliseconds(1_785_000_000_000)
    private val clock = mockk<Clock> { every { now() } returns now }

    private val entries = MutableStateFlow<List<DexEntry>>(emptyList())
    private val repository = mockk<DexRepository> { every { observeAll() } returns entries }

    private val subject = GetDexStatsUseCase(repository, clock)

    private fun plant(daysAgo: Int, category: PlantCategory) = Plant(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "열대 관엽식물",
        origin = "멕시코 남부",
        watering = "2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        difficulty = 2,
        discoveredAt = now - daysAgo.days,
        category = category,
    )

    private fun given(vararg daysAgo: Int, category: PlantCategory = PlantCategory.FOLIAGE) {
        entries.value = daysAgo.mapIndexed { index, age ->
            DexEntry(id = index.toLong(), dexNumber = index + 1, plant = plant(age, category))
        }
    }

    private suspend fun stats() = subject().first()

    /** 월별 차트가 덮는 달 목록. 과거 → 현재 순. */
    private fun expectedMonths(): List<Int> {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = now.toEpochMilliseconds()
            set(Calendar.DAY_OF_MONTH, 1)
            add(Calendar.MONTH, -(MONTHLY_SPAN - 1))
        }
        return List(MONTHLY_SPAN) {
            val month = calendar.get(Calendar.MONTH) + 1
            calendar.add(Calendar.MONTH, 1)
            month
        }
    }

    @Test
    fun `도감이 비면 연도만 채우고 나머지는 기본값이다`() = runTest {
        val stats = stats()

        assertEquals(0, stats.totalCount)
        assertEquals(0, stats.streakDays)
        assertEquals(0, stats.longestStreakDays)
        assertEquals(emptyList<Any>(), stats.monthly)
        assertEquals(emptyList<Any>(), stats.byCategory)
    }

    @Test
    fun `오늘 발견했으면 연속 기록은 1 이다`() = runTest {
        given(0)

        assertEquals(1, stats().streakDays)
    }

    @Test
    fun `사흘 내리 발견했으면 연속 기록은 3 이다`() = runTest {
        given(0, 1, 2)

        assertEquals(3, stats().streakDays)
    }

    @Test
    fun `하루가 비면 거기서 연속 기록이 끊긴다`() = runTest {
        given(0, 1, 3, 4)

        assertEquals(2, stats().streakDays)
    }

    @Test
    fun `오늘 발견이 없어도 어제부터 세서 연속 기록이 유지된다`() = runTest {
        given(1, 2)

        assertEquals(2, stats().streakDays)
    }

    @Test
    fun `이틀 넘게 비면 연속 기록이 0 이다`() = runTest {
        given(2, 3)

        assertEquals(0, stats().streakDays)
    }

    @Test
    fun `같은 날 여러 개를 발견해도 연속 기록은 하루로 센다`() = runTest {
        given(0, 0, 1)

        assertEquals(2, stats().streakDays)
    }

    @Test
    fun `최장 기록은 현재 연속보다 길 수 있다`() = runTest {
        // 지금은 이틀 연속이지만 과거에 나흘 연속이 있었다.
        given(0, 1, 10, 11, 12, 13)

        val stats = stats()

        assertEquals(2, stats.streakDays)
        assertEquals(4, stats.longestStreakDays)
    }

    @Test
    fun `월별은 항상 6개월이고 발견이 없는 달은 0 이다`() = runTest {
        given(0)

        val monthly = stats().monthly

        assertEquals(MONTHLY_SPAN, monthly.size)
        assertEquals(expectedMonths(), monthly.map { it.month })
        assertEquals(1, monthly.last().count)
        assertEquals(0, monthly.dropLast(1).sumOf { it.count })
    }

    @Test
    fun `6개월보다 오래된 발견은 월별에서 빠진다`() = runTest {
        given(0, 300)

        val stats = stats()

        assertEquals(2, stats.totalCount)
        assertEquals(1, stats.monthly.sumOf { it.count })
    }

    @Test
    fun `분류별은 개수 내림차순이다`() = runTest {
        entries.value = listOf(
            DexEntry(id = 1, dexNumber = 1, plant = plant(0, PlantCategory.WILDFLOWER)),
            DexEntry(id = 2, dexNumber = 2, plant = plant(1, PlantCategory.FOLIAGE)),
            DexEntry(id = 3, dexNumber = 3, plant = plant(2, PlantCategory.FOLIAGE)),
            DexEntry(id = 4, dexNumber = 4, plant = plant(3, PlantCategory.FOLIAGE)),
            DexEntry(id = 5, dexNumber = 5, plant = plant(4, PlantCategory.WILDFLOWER)),
        )

        assertEquals(
            listOf(
                CategoryCount(PlantCategory.FOLIAGE, 3),
                CategoryCount(PlantCategory.WILDFLOWER, 2),
            ),
            stats().byCategory,
        )
    }

    @Test
    fun `발견이 없는 분류는 목록에 넣지 않는다`() = runTest {
        given(0, category = PlantCategory.BULB)

        assertEquals(listOf(CategoryCount(PlantCategory.BULB, 1)), stats().byCategory)
    }

    @Test
    fun `도감이 바뀌면 통계도 다시 흐른다`() = runTest {
        given(0)
        assertEquals(1, stats().totalCount)

        given(0, 1, 2)
        assertEquals(3, stats().totalCount)
    }
}
