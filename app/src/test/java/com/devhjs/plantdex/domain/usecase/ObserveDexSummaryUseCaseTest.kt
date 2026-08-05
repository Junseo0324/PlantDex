package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.repository.DexRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class ObserveDexSummaryUseCaseTest {

    private val now = Instant.fromEpochMilliseconds(1_785_000_000_000)
    private val clock = mockk<Clock> { every { now() } returns now }

    private val entries = MutableStateFlow<List<DexEntry>>(emptyList())
    private val repository = mockk<DexRepository> {
        every { observeAll() } returns entries.map { list ->
            list.sortedByDescending(DexEntry::dexNumber)
        }
    }

    private val subject = ObserveDexSummaryUseCase(repository, clock)

    private fun plant(discoveredAt: Instant) = Plant(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "열대 관엽식물",
        origin = "멕시코 남부",
        watering = "2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        difficulty = 2,
        discoveredAt = discoveredAt,
    )

    private fun entry(number: Int, daysAgo: Int = 1) = DexEntry(
        id = number.toLong(),
        dexNumber = number,
        plant = plant(now - daysAgo.days),
    )

    private suspend fun summary(recentLimit: Int = 3) = subject(recentLimit).first()

    @Test
    fun `도감이 비면 전부 0 이다`() = runTest {
        val summary = summary()

        assertEquals(0, summary.total)
        assertEquals(0, summary.thisMonth)
        assertEquals(emptyList<DexEntry>(), summary.recent)
    }

    @Test
    fun `total 은 도감 전체 개수다`() = runTest {
        entries.value = List(7) { entry(number = it + 1) }

        assertEquals(7, summary().total)
    }

    @Test
    fun `recent 는 도감 번호 내림차순 상위 limit 개다`() = runTest {
        entries.value = List(7) { entry(number = it + 1) }

        assertEquals(listOf(7, 6, 5), summary(recentLimit = 3).recent.map(DexEntry::dexNumber))
    }

    @Test
    fun `도감이 limit 보다 적으면 있는 만큼만 담는다`() = runTest {
        entries.value = listOf(entry(number = 1), entry(number = 2))

        assertEquals(listOf(2, 1), summary(recentLimit = 3).recent.map(DexEntry::dexNumber))
    }

    @Test
    fun `thisMonth 는 주입된 시계와 같은 달만 센다`() = runTest {
        entries.value = listOf(
            entry(number = 1, daysAgo = 1),
            entry(number = 2, daysAgo = 2),
            entry(number = 3, daysAgo = 200),
            entry(number = 4, daysAgo = 400),
        )

        val summary = summary()

        assertEquals(4, summary.total)
        assertEquals(2, summary.thisMonth)
    }

    @Test
    fun `이번 달 발견이 없으면 thisMonth 는 0 이다`() = runTest {
        entries.value = listOf(entry(number = 1, daysAgo = 200))

        val summary = summary()

        assertEquals(1, summary.total)
        assertEquals(0, summary.thisMonth)
    }

    @Test
    fun `도감이 바뀌면 요약도 다시 흘러나온다`() = runTest {
        entries.value = listOf(entry(number = 1))
        assertEquals(1, summary().total)

        entries.value = listOf(entry(number = 1), entry(number = 2))

        assertEquals(2, summary().total)
    }
}
