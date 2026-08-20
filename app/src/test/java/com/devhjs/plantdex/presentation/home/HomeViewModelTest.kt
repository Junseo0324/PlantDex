package com.devhjs.plantdex.presentation.home

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.DexSummary
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.usecase.GetDexSummaryUseCase
import com.devhjs.plantdex.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val summaries = MutableStateFlow(DexSummary())
    private val getDexSummary = mockk<GetDexSummaryUseCase> {
        every { this@mockk.invoke(any()) } returns summaries
    }

    private fun subject() = HomeViewModel(getDexSummary)

    private fun entry(number: Int) = DexEntry(
        id = number.toLong(),
        dexNumber = number,
        plant = Plant(
            name = "몬스테라",
            englishName = "Monstera deliciosa",
            description = "열대 관엽식물",
            origin = "멕시코 남부",
            watering = "2주에 한 번",
            sunlight = Sunlight.BRIGHT_INDIRECT,
            difficulty = 2,
            discoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000),
        ),
    )

    @Test
    fun `초기 상태는 기본값이다`() = runTest {
        assertEquals(HomeState(), subject().state.value)
    }

    @Test
    fun `요약이 상태로 옮겨진다`() = runTest {
        summaries.value = DexSummary(
            total = 23,
            thisMonth = 6,
            recent = listOf(entry(23), entry(22), entry(21)),
        )

        val state = subject().state.first { it != HomeState() }

        assertEquals(23, state.discoveredCount)
        assertEquals(6, state.thisMonthCount)
        assertEquals(listOf(23, 22, 21), state.recent.map(DexEntry::dexNumber))
    }

    @Test
    fun `요약이 바뀌면 상태가 따라 바뀐다`() = runTest {
        val viewModel = subject()
        summaries.value = DexSummary(total = 1)
        assertEquals(1, viewModel.state.first { it.discoveredCount == 1 }.discoveredCount)

        summaries.value = DexSummary(total = 2)

        assertEquals(2, viewModel.state.first { it.discoveredCount == 2 }.discoveredCount)
    }

    @Test
    fun `화면이 그리는 칸 수만큼 요약을 요청한다`() = runTest {
        subject()
        advanceUntilIdle()

        verify(exactly = 1) { getDexSummary(RECENT_LIMIT) }
    }

    private fun TestScope.collectEvents(viewModel: HomeViewModel): List<HomeEvent> {
        val events = mutableListOf<HomeEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.event.toList(events)
        }
        return events
    }

    @Test
    fun `Discover 는 카메라로 가는 이벤트를 낸다`() = runTest {
        val viewModel = subject()
        val events = collectEvents(viewModel)

        viewModel.onAction(HomeAction.Discover)
        advanceUntilIdle()

        assertEquals(listOf(HomeEvent.NavigateToCamera), events)
    }

    @Test
    fun `SeeAllCollection 은 도감 탭으로 가는 이벤트를 낸다`() = runTest {
        val viewModel = subject()
        val events = collectEvents(viewModel)

        viewModel.onAction(HomeAction.SeeAllCollection)
        advanceUntilIdle()

        assertEquals(listOf(HomeEvent.NavigateToCollection), events)
    }

    @Test
    fun `OpenDetail 은 눌린 항목의 id 를 실어 보낸다`() = runTest {
        val viewModel = subject()
        val events = collectEvents(viewModel)

        viewModel.onAction(HomeAction.OpenDetail(7L))
        advanceUntilIdle()

        assertEquals(listOf(HomeEvent.NavigateToDetail(7L)), events)
    }
}
