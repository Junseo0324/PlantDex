package com.devhjs.plantdex.presentation.analyze

import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.domain.datasource.PhotoLoader
import com.devhjs.plantdex.domain.datasource.PlantAnalyzer
import com.devhjs.plantdex.domain.mapper.toPlant
import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.PlantAnalysis
import com.devhjs.plantdex.domain.model.PlantPhoto
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.usecase.AnalyzePlantPhotoUseCase
import com.devhjs.plantdex.domain.usecase.DeletePhotoUseCase
import com.devhjs.plantdex.domain.usecase.GetNextDexNumberUseCase
import com.devhjs.plantdex.domain.usecase.RegisterDexEntryUseCase
import com.devhjs.plantdex.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

/**
 * 촬영을 마치고 들어오는 화면이라 Root 가 Start 로 사진을 넘기면서 분석이 시작된다.
 * 대부분의 테스트는 그 상태가 출발점이라 [started] 를 쓴다.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AnalyzeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private companion object {
        const val PHOTO_URI = "file:///tmp/shot.jpg"
    }

    private val analysis = PlantAnalysis(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "열대 관엽식물",
        origin = "멕시코 남부",
        watering = "2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        rawDifficulty = 2,
    )

    private val photo = PlantPhoto(ByteArray(8))
    private val photoLoader = mockk<PhotoLoader> {
        coEvery { load(any()) } returns photo
    }
    private val analyzer = mockk<PlantAnalyzer> {
        coEvery { analyze(any()) } returns Result.Success(analysis)
    }
    private val clock = mockk<Clock> {
        every { now() } returns Instant.fromEpochMilliseconds(1_700_000_000_000)
    }

    private val registeredEntry = DexEntry(
        id = 5L,
        dexNumber = 5,
        plant = analysis.toPlant(Instant.fromEpochMilliseconds(1_700_000_000_000)),
    )

    private val getNextDexNumber = mockk<GetNextDexNumberUseCase> {
        coEvery { this@mockk() } returns 5
    }
    private val registerDexEntry = mockk<RegisterDexEntryUseCase> {
        coEvery { this@mockk(any(), any()) } returns registeredEntry
    }
    private val deletePhoto = mockk<DeletePhotoUseCase> {
        coEvery { this@mockk(any()) } returns Unit
    }

    private fun subject() = AnalyzeViewModel(
        AnalyzePlantPhotoUseCase(photoLoader, analyzer, clock),
        getNextDexNumber,
        registerDexEntry,
        deletePhoto,
    )

    /** Root 가 화면 진입 시 보내는 Start 까지 마친 ViewModel. */
    private fun started(photoUri: String? = PHOTO_URI) =
        subject().apply { onAction(AnalyzeAction.Start(photoUri)) }

    private fun TestScope.collectEvents(viewModel: AnalyzeViewModel): List<AnalyzeEvent> {
        val events = mutableListOf<AnalyzeEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.event.toList(events)
        }
        return events
    }

    private fun givenError(error: AnalysisError) {
        coEvery { analyzer.analyze(any()) } returns Result.Error(error)
    }

    private fun TestScope.collectStates(viewModel: AnalyzeViewModel): List<AnalyzeState> {
        val states = mutableListOf<AnalyzeState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(states)
        }
        return states
    }

    @Test
    fun `초기 상태는 Loading 이다`() = runTest {
        assertEquals(AnalyzeState.Loading, subject().state.value)
    }

    @Test
    fun `Start 전에는 분석하지 않는다`() = runTest {
        subject()
        advanceUntilIdle()

        coVerify(exactly = 0) { analyzer.analyze(any()) }
    }

    @Test
    fun `Start 를 받으면 분석이 시작된다`() = runTest {
        val viewModel = started()

        advanceUntilIdle()

        coVerify(exactly = 1) { analyzer.analyze(any()) }
        assertTrue(viewModel.state.value is AnalyzeState.Success)
    }

    /** Root 는 화면에 다시 들어올 때마다 Start 를 보낸다. */
    @Test
    fun `Start 를 다시 받아도 재분석하지 않는다`() = runTest {
        val viewModel = started()
        advanceUntilIdle()

        repeat(3) { viewModel.onAction(AnalyzeAction.Start(PHOTO_URI)) }
        advanceUntilIdle()

        coVerify(exactly = 1) { analyzer.analyze(any()) }
    }

    @Test
    fun `성공하면 Loading 다음 Success 로 바뀐다`() = runTest {
        val viewModel = started()
        val states = collectStates(viewModel)

        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AnalyzeState.Loading, states[0])
        assertEquals("몬스테라", (states[1] as AnalyzeState.Success).plant.name)
    }

    @Test
    fun `실패하면 Loading 다음 Error 로 바뀌고 에러 종류가 유지된다`() = runTest {
        givenError(AnalysisError.NotAPlant)
        val viewModel = started()
        val states = collectStates(viewModel)

        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AnalyzeState.Loading, states[0])
        assertEquals(AnalyzeState.Error(AnalysisError.NotAPlant), states[1])
    }

    @Test
    fun `발견일이 결과에 반영된다`() = runTest {
        val now = Instant.fromEpochMilliseconds(1_234_567_890)
        every { clock.now() } returns now
        val viewModel = started()

        advanceUntilIdle()

        assertEquals(now, (viewModel.state.value as AnalyzeState.Success).plant.discoveredAt)
    }

    @Test
    fun `분석 중에 재시도를 보내도 한 번만 실행된다`() = runTest {
        coEvery { analyzer.analyze(any()) } coAnswers {
            delay(500.milliseconds)
            Result.Success(analysis)
        }
        val viewModel = started()

        repeat(3) { viewModel.onAction(AnalyzeAction.Retry) }
        advanceUntilIdle()

        coVerify(exactly = 1) { analyzer.analyze(any()) }
        assertTrue(viewModel.state.value is AnalyzeState.Success)
    }

    @Test
    fun `실패한 뒤 재시도하면 다시 분석한다`() = runTest {
        givenError(AnalysisError.Network)
        val viewModel = started()
        advanceUntilIdle()
        assertEquals(AnalyzeState.Error(AnalysisError.Network), viewModel.state.value)

        coEvery { analyzer.analyze(any()) } returns Result.Success(analysis)
        viewModel.onAction(AnalyzeAction.Retry)
        advanceUntilIdle()

        coVerify(exactly = 2) { analyzer.analyze(any()) }
        assertTrue(viewModel.state.value is AnalyzeState.Success)
    }

    @Test
    fun `재시도하면 Loading 을 다시 거친다`() = runTest {
        givenError(AnalysisError.Network)
        val viewModel = started()
        val states = collectStates(viewModel)
        advanceUntilIdle()

        coEvery { analyzer.analyze(any()) } returns Result.Success(analysis)
        viewModel.onAction(AnalyzeAction.Retry)
        advanceUntilIdle()

        // Loading -> Error -> Loading -> Success
        assertEquals(4, states.size)
        assertEquals(AnalyzeState.Loading, states[2])
        assertTrue(states[3] is AnalyzeState.Success)
    }

    @Test
    fun `촬영한 사진 위치에서 읽어 분석기로 넘긴다`() = runTest {
        val loaded = slot<PlantPhoto>()
        started()

        advanceUntilIdle()

        coVerify(exactly = 1) { photoLoader.load(PHOTO_URI) }
        coVerify { analyzer.analyze(capture(loaded)) }
        assertEquals(photo, loaded.captured)
    }

    @Test
    fun `사진을 읽지 못하면 분석하지 않고 에러 상태가 된다`() = runTest {
        coEvery { photoLoader.load(any()) } returns null
        val viewModel = started()

        advanceUntilIdle()

        assertEquals(AnalyzeState.Error(AnalysisError.PhotoUnavailable), viewModel.state.value)
        coVerify(exactly = 0) { analyzer.analyze(any()) }
    }

    @Test
    fun `사진 없이 들어오면 에러 상태가 된다`() = runTest {
        val viewModel = started(photoUri = null)

        advanceUntilIdle()

        assertEquals(AnalyzeState.Error(AnalysisError.PhotoUnavailable), viewModel.state.value)
        coVerify(exactly = 0) { photoLoader.load(any()) }
    }

    @Test
    fun `성공 상태에 등록하면 받게 될 번호가 실린다`() = runTest {
        coEvery { getNextDexNumber() } returns 7
        val viewModel = started()

        advanceUntilIdle()

        assertEquals(7, (viewModel.state.value as AnalyzeState.Success).nextDexNumber)
    }

    @Test
    fun `Register 는 분석된 식물을 도감에 등록한다`() = runTest {
        val viewModel = started()
        advanceUntilIdle()

        viewModel.onAction(AnalyzeAction.Register)
        advanceUntilIdle()

        coVerify(exactly = 1) { registerDexEntry(match { it.name == "몬스테라" }, PHOTO_URI) }
    }

    @Test
    fun `등록되면 Registered 이벤트가 나온다`() = runTest {
        val viewModel = started()
        val events = collectEvents(viewModel)
        advanceUntilIdle()

        viewModel.onAction(AnalyzeAction.Register)
        advanceUntilIdle()

        assertEquals(listOf(AnalyzeEvent.Registered(5L)), events)
    }

    @Test
    fun `성공하기 전에는 Register 가 아무것도 하지 않는다`() = runTest {
        givenError(AnalysisError.Network)
        val viewModel = started()
        advanceUntilIdle()

        viewModel.onAction(AnalyzeAction.Register)
        advanceUntilIdle()

        coVerify(exactly = 0) { registerDexEntry(any(), any()) }
    }

    @Test
    fun `등록을 연타해도 한 번만 등록된다`() = runTest {
        coEvery { registerDexEntry(any(), any()) } coAnswers {
            delay(500.milliseconds)
            registeredEntry
        }
        val viewModel = started()
        advanceUntilIdle()

        repeat(3) { viewModel.onAction(AnalyzeAction.Register) }
        advanceUntilIdle()

        coVerify(exactly = 1) { registerDexEntry(any(), any()) }
    }

    @Test
    fun `Retake 는 재촬영 이벤트를 낸다`() = runTest {
        val viewModel = started()
        val events = collectEvents(viewModel)
        advanceUntilIdle()
        val before = viewModel.state.value

        viewModel.onAction(AnalyzeAction.Retake)
        advanceUntilIdle()

        assertEquals(listOf(AnalyzeEvent.Retake), events)
        assertEquals(before, viewModel.state.value)
        coVerify(exactly = 0) { registerDexEntry(any(), any()) }
    }

    /** 등록하지 않고 나가므로 방금 찍은 사진이 저장소에 남으면 안 된다. */
    @Test
    fun `Retake 는 찍은 사진을 지운다`() = runTest {
        val viewModel = started()
        advanceUntilIdle()

        viewModel.onAction(AnalyzeAction.Retake)
        advanceUntilIdle()

        coVerify(exactly = 1) { deletePhoto(PHOTO_URI) }
    }

    @Test
    fun `등록하면 사진을 지우지 않는다`() = runTest {
        val viewModel = started()
        advanceUntilIdle()

        viewModel.onAction(AnalyzeAction.Register)
        advanceUntilIdle()

        coVerify(exactly = 0) { deletePhoto(any()) }
    }

    @Test
    fun `성공 상태에 찍은 사진 위치가 실린다`() = runTest {
        val viewModel = started()

        advanceUntilIdle()

        assertEquals(PHOTO_URI, (viewModel.state.value as AnalyzeState.Success).photoUri)
    }
}
