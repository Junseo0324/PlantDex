package com.devhjs.plantdex.presentation.record

import com.devhjs.plantdex.domain.model.CategoryCount
import com.devhjs.plantdex.domain.model.DexStats
import com.devhjs.plantdex.domain.model.MonthlyCount
import com.devhjs.plantdex.domain.model.PlantCategory
import com.devhjs.plantdex.domain.usecase.GetDexStatsUseCase
import com.devhjs.plantdex.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RecordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val stats = MutableStateFlow(DexStats())
    private val getDexStats = mockk<GetDexStatsUseCase> {
        every { this@mockk.invoke() } returns stats
    }

    private fun subject() = RecordViewModel(getDexStats)

    @Test
    fun `초기 상태는 기본값이다`() = runTest {
        assertEquals(RecordState(), subject().state.value)
    }

    @Test
    fun `통계가 상태로 옮겨진다`() = runTest {
        stats.value = DexStats(
            year = 2026,
            totalCount = 7,
            streakDays = 3,
            longestStreakDays = 12,
            monthly = listOf(MonthlyCount(month = 8, count = 4)),
            byCategory = listOf(CategoryCount(PlantCategory.FOLIAGE, 4)),
        )

        val state = subject().state.first { it != RecordState() }

        assertEquals(2026, state.stats.year)
        assertEquals(7, state.stats.totalCount)
        assertEquals(3, state.stats.streakDays)
        assertEquals(12, state.stats.longestStreakDays)
        assertEquals(listOf(8), state.stats.monthly.map(MonthlyCount::month))
        assertEquals(listOf(4), state.stats.byCategory.map(CategoryCount::count))
    }

    @Test
    fun `도감이 바뀌면 통계도 따라 바뀐다`() = runTest {
        val viewModel = subject()
        stats.value = DexStats(totalCount = 1)
        assertEquals(1, viewModel.state.first { it.stats.totalCount == 1 }.stats.totalCount)

        stats.value = DexStats(totalCount = 2)

        assertEquals(2, viewModel.state.first { it.stats.totalCount == 2 }.stats.totalCount)
    }

    @Test
    fun `통계는 한 번만 구독한다`() = runTest {
        subject()
        advanceUntilIdle()

        verify(exactly = 1) { getDexStats() }
    }
}
