package com.devhjs.plantdex.presentation.collection

import com.devhjs.plantdex.domain.model.DexCollection
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.usecase.GetDexCollectionUseCase
import com.devhjs.plantdex.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
class CollectionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val discoveredAt = Instant.fromEpochMilliseconds(1_785_000_000_000)

    private fun entry(number: Int, name: String) = DexEntry(
        id = number.toLong(),
        dexNumber = number,
        plant = Plant(
            name = name,
            englishName = "Monstera deliciosa",
            description = "열대 관엽식물",
            origin = "멕시코 남부",
            watering = "2주에 한 번",
            sunlight = Sunlight.BRIGHT_INDIRECT,
            difficulty = 2,
            discoveredAt = discoveredAt,
        ),
    )

    private val collections = MutableStateFlow(DexCollection())
    private val getDexCollection = mockk<GetDexCollectionUseCase> {
        every { this@mockk(any(), any()) } returns collections
    }

    private fun subject() = CollectionViewModel(getDexCollection)

    @Test
    fun `초기 상태는 기본값이다`() = runTest {
        assertEquals(CollectionState(), subject().state.value)
    }

    @Test
    fun `생성 시 빈 검색어와 전체 필터로 조회한다`() = runTest {
        subject()
        advanceUntilIdle()

        verify(exactly = 1) { getDexCollection("", false) }
    }

    @Test
    fun `도감이 흘러오면 상태에 반영된다`() = runTest {
        collections.value = DexCollection(
            entries = listOf(entry(2, "산세베리아"), entry(1, "몬스테라")),
            totalCount = 5,
            lastDiscoveredAt = discoveredAt,
        )

        val viewModel = subject()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(listOf(2, 1), state.entries.map(DexEntry::dexNumber))
        assertEquals(5, state.totalCount)
        assertEquals(discoveredAt, state.lastDiscoveredAt)
    }

    @Test
    fun `QueryChanged 가 검색어를 갱신하고 다시 조회한다`() = runTest {
        val viewModel = subject()
        advanceUntilIdle()

        viewModel.onAction(CollectionAction.QueryChanged("몬스"))
        advanceUntilIdle()

        assertEquals("몬스", viewModel.state.value.query)
        verify(exactly = 1) { getDexCollection("몬스", false) }
    }

    @Test
    fun `FilterChanged 가 필터를 갱신하고 즐겨찾기로 다시 조회한다`() = runTest {
        val viewModel = subject()
        advanceUntilIdle()

        viewModel.onAction(CollectionAction.FilterChanged(CollectionFilter.Favorites))
        advanceUntilIdle()

        assertEquals(CollectionFilter.Favorites, viewModel.state.value.filter)
        verify(exactly = 1) { getDexCollection("", true) }
    }

    @Test
    fun `검색어와 필터가 함께 전달된다`() = runTest {
        val viewModel = subject()
        advanceUntilIdle()

        viewModel.onAction(CollectionAction.QueryChanged("몬스"))
        viewModel.onAction(CollectionAction.FilterChanged(CollectionFilter.Favorites))
        advanceUntilIdle()

        verify(exactly = 1) { getDexCollection("몬스", true) }
    }

    @Test
    fun `같은 검색어를 다시 넣으면 재조회하지 않는다`() = runTest {
        val viewModel = subject()
        advanceUntilIdle()

        repeat(3) { viewModel.onAction(CollectionAction.QueryChanged("몬스")) }
        advanceUntilIdle()

        verify(exactly = 1) { getDexCollection("몬스", false) }
    }

    /** distinctUntilChanged 가 빠지면 결과 반영이 다시 조회를 불러 무한 루프가 된다. */
    @Test
    fun `결과만 바뀌는 갱신은 재조회를 유발하지 않는다`() = runTest {
        val viewModel = subject()
        advanceUntilIdle()

        repeat(5) { round ->
            collections.value = DexCollection(
                entries = listOf(entry(round + 1, "식물$round")),
                totalCount = round + 1,
            )
            advanceUntilIdle()
        }

        assertEquals(5, viewModel.state.value.totalCount)
        verify(exactly = 1) { getDexCollection("", false) }
    }

    private fun TestScope.collectEvents(viewModel: CollectionViewModel): List<CollectionEvent> {
        val events = mutableListOf<CollectionEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.event.toList(events)
        }
        return events
    }

    @Test
    fun `OpenDetail 은 눌린 항목의 id 를 실어 보낸다`() = runTest {
        collections.value = DexCollection(entries = listOf(entry(1, "몬스테라")), totalCount = 1)
        val viewModel = subject()
        val events = collectEvents(viewModel)
        advanceUntilIdle()

        viewModel.onAction(CollectionAction.OpenDetail(1L))
        advanceUntilIdle()

        assertEquals(listOf(CollectionEvent.NavigateToDetail(1L)), events)
    }

    @Test
    fun `OpenDetail 은 상태나 재조회를 건드리지 않는다`() = runTest {
        collections.value = DexCollection(entries = listOf(entry(1, "몬스테라")), totalCount = 1)
        val viewModel = subject()
        advanceUntilIdle()
        val before = viewModel.state.value

        viewModel.onAction(CollectionAction.OpenDetail(1L))
        advanceUntilIdle()

        assertEquals(before, viewModel.state.value)
        verify(exactly = 1) { getDexCollection(any(), any()) }
    }
}
