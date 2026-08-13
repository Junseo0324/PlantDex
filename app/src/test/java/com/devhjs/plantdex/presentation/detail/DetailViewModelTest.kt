package com.devhjs.plantdex.presentation.detail

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.usecase.ObserveDexEntryUseCase
import com.devhjs.plantdex.domain.usecase.SaveMemoUseCase
import com.devhjs.plantdex.domain.usecase.SetFavoriteUseCase
import com.devhjs.plantdex.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.time.Instant

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private companion object {
        const val ENTRY_ID = 4L
    }

    private fun entry(
        isFavorite: Boolean = false,
        memo: String? = null,
    ) = DexEntry(
        id = ENTRY_ID,
        dexNumber = 4,
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
        isFavorite = isFavorite,
        memo = memo,
    )

    private val observed = MutableStateFlow<DexEntry?>(null)
    private val observeDexEntry = mockk<ObserveDexEntryUseCase> {
        every { this@mockk(any()) } returns observed
    }
    private val setFavorite = mockk<SetFavoriteUseCase> {
        coEvery { this@mockk(any(), any()) } returns Unit
    }
    private val saveMemo = mockk<SaveMemoUseCase> {
        coEvery { this@mockk(any(), any()) } returns Unit
    }

    private fun subject() = DetailViewModel(observeDexEntry, setFavorite, saveMemo)

    private fun loadedSubject() = subject().apply { load(ENTRY_ID) }

    @Test
    fun `초기 상태는 기본값이다`() = runTest {
        assertEquals(DetailState(), subject().state.value)
    }

    @Test
    fun `load 전에는 조회하지 않는다`() = runTest {
        subject()
        advanceUntilIdle()

        verify(exactly = 0) { observeDexEntry(any()) }
    }

    @Test
    fun `load 로 받은 id 로 조회한다`() = runTest {
        loadedSubject()
        advanceUntilIdle()

        verify(exactly = 1) { observeDexEntry(ENTRY_ID) }
    }

    @Test
    fun `같은 id 로 다시 load 해도 재구독하지 않는다`() = runTest {
        val viewModel = loadedSubject()
        advanceUntilIdle()

        repeat(3) { viewModel.load(ENTRY_ID) }
        advanceUntilIdle()

        verify(exactly = 1) { observeDexEntry(ENTRY_ID) }
    }

    @Test
    fun `다른 id 로 load 하면 새로 구독한다`() = runTest {
        val viewModel = loadedSubject()
        advanceUntilIdle()

        viewModel.load(99L)
        advanceUntilIdle()

        verify(exactly = 1) { observeDexEntry(ENTRY_ID) }
        verify(exactly = 1) { observeDexEntry(99L) }
    }

    @Test
    fun `항목이 상태로 들어온다`() = runTest {
        val target = entry()
        observed.value = target

        val viewModel = loadedSubject()
        advanceUntilIdle()

        assertEquals(target, viewModel.state.value.entry)
    }

    @Test
    fun `없는 항목이면 entry 가 null 로 남는다`() = runTest {
        val viewModel = loadedSubject()
        advanceUntilIdle()

        assertNull(viewModel.state.value.entry)
    }

    @Test
    fun `저장소가 갱신되면 상태도 따라 바뀐다`() = runTest {
        observed.value = entry()
        val viewModel = loadedSubject()
        advanceUntilIdle()

        observed.value = entry(isFavorite = true)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.entry!!.isFavorite)
    }

    @Test
    fun `ToggleFavorite 은 현재값의 반대로 요청한다`() = runTest {
        observed.value = entry(isFavorite = false)
        val viewModel = loadedSubject()
        advanceUntilIdle()

        viewModel.onAction(DetailAction.ToggleFavorite)
        advanceUntilIdle()

        coVerify(exactly = 1) { setFavorite(ENTRY_ID, true) }
    }

    @Test
    fun `이미 즐겨찾기면 해제를 요청한다`() = runTest {
        observed.value = entry(isFavorite = true)
        val viewModel = loadedSubject()
        advanceUntilIdle()

        viewModel.onAction(DetailAction.ToggleFavorite)
        advanceUntilIdle()

        coVerify(exactly = 1) { setFavorite(ENTRY_ID, false) }
    }

    @Test
    fun `항목이 없으면 ToggleFavorite 이 아무것도 하지 않는다`() = runTest {
        val viewModel = loadedSubject()
        advanceUntilIdle()

        viewModel.onAction(DetailAction.ToggleFavorite)
        advanceUntilIdle()

        coVerify(exactly = 0) { setFavorite(any(), any()) }
    }

    @Test
    fun `OpenMemoEditor 가 기존 메모를 draft 에 채운다`() = runTest {
        observed.value = entry(memo = "창가 화분")
        val viewModel = loadedSubject()
        advanceUntilIdle()

        viewModel.onAction(DetailAction.OpenMemoEditor)

        val state = viewModel.state.value
        assertTrue(state.isMemoEditorOpen)
        assertEquals("창가 화분", state.memoDraft)
    }

    @Test
    fun `메모가 없으면 draft 는 빈 문자열로 열린다`() = runTest {
        observed.value = entry(memo = null)
        val viewModel = loadedSubject()
        advanceUntilIdle()

        viewModel.onAction(DetailAction.OpenMemoEditor)

        assertEquals("", viewModel.state.value.memoDraft)
    }

    @Test
    fun `MemoDraftChanged 가 draft 를 갱신한다`() = runTest {
        val viewModel = loadedSubject()

        viewModel.onAction(DetailAction.MemoDraftChanged("물 줌"))

        assertEquals("물 줌", viewModel.state.value.memoDraft)
    }

    @Test
    fun `SaveMemo 가 draft 를 저장하고 에디터를 닫는다`() = runTest {
        observed.value = entry()
        val viewModel = loadedSubject()
        advanceUntilIdle()
        viewModel.onAction(DetailAction.OpenMemoEditor)
        viewModel.onAction(DetailAction.MemoDraftChanged("창가로 옮김"))

        viewModel.onAction(DetailAction.SaveMemo)
        advanceUntilIdle()

        coVerify(exactly = 1) { saveMemo(ENTRY_ID, "창가로 옮김") }
        val state = viewModel.state.value
        assertFalse(state.isMemoEditorOpen)
        assertEquals("", state.memoDraft)
    }

    @Test
    fun `DismissMemoEditor 는 저장하지 않고 draft 를 버린다`() = runTest {
        observed.value = entry()
        val viewModel = loadedSubject()
        advanceUntilIdle()
        viewModel.onAction(DetailAction.OpenMemoEditor)
        viewModel.onAction(DetailAction.MemoDraftChanged("쓰다 말았다"))

        viewModel.onAction(DetailAction.DismissMemoEditor)
        advanceUntilIdle()

        coVerify(exactly = 0) { saveMemo(any(), any()) }
        val state = viewModel.state.value
        assertFalse(state.isMemoEditorOpen)
        assertEquals("", state.memoDraft)
    }

    @Test
    fun `Back 은 ViewModel 에서 아무 일도 하지 않는다`() = runTest {
        observed.value = entry()
        val viewModel = loadedSubject()
        advanceUntilIdle()
        val before = viewModel.state.value

        viewModel.onAction(DetailAction.Back)
        advanceUntilIdle()

        assertEquals(before, viewModel.state.value)
        coVerify(exactly = 0) { setFavorite(any(), any()) }
        coVerify(exactly = 0) { saveMemo(any(), any()) }
    }
}
