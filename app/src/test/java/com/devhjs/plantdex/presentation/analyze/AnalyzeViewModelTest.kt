package com.devhjs.plantdex.presentation.analyze

import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.domain.datasource.PlantAnalyzer
import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.PlantAnalysis
import com.devhjs.plantdex.domain.model.PlantPhoto
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.usecase.AnalyzePlantPhotoUseCase
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
 * 촬영을 마치고 들어오는 화면이라 ViewModel 이 init 에서 분석을 시작한다.
 *
 * 그래서 subject 를 필드로 만들면 안 된다 — 필드 초기화는 MainDispatcherRule 이
 * Dispatchers.setMain 을 걸기 전에 실행돼서 init 의 코루틴이 실제 Main 으로 새어나간다.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AnalyzeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val analysis = PlantAnalysis(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "열대 관엽식물",
        origin = "멕시코 남부",
        watering = "2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        rawDifficulty = 2,
    )

    private val analyzer = mockk<PlantAnalyzer> {
        coEvery { analyze(any()) } returns Result.Success(analysis)
    }
    private val clock = mockk<Clock> {
        every { now() } returns Instant.fromEpochMilliseconds(1_700_000_000_000)
    }

    private fun subject() = AnalyzeViewModel(AnalyzePlantPhotoUseCase(analyzer, clock))

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
    fun `생성되면 따로 요청하지 않아도 분석이 시작된다`() = runTest {
        val viewModel = subject()

        advanceUntilIdle()

        coVerify(exactly = 1) { analyzer.analyze(any()) }
        assertTrue(viewModel.state.value is AnalyzeState.Success)
    }

    @Test
    fun `성공하면 Loading 다음 Success 로 바뀐다`() = runTest {
        val viewModel = subject()
        val states = collectStates(viewModel)

        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AnalyzeState.Loading, states[0])
        assertEquals("몬스테라", (states[1] as AnalyzeState.Success).plant.name)
    }

    @Test
    fun `실패하면 Loading 다음 Error 로 바뀌고 에러 종류가 유지된다`() = runTest {
        givenError(AnalysisError.NotAPlant)
        val viewModel = subject()
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
        val viewModel = subject()

        advanceUntilIdle()

        assertEquals(now, (viewModel.state.value as AnalyzeState.Success).plant.discoveredAt)
    }

    @Test
    fun `분석 중에 재시도를 보내도 한 번만 실행된다`() = runTest {
        coEvery { analyzer.analyze(any()) } coAnswers {
            delay(500.milliseconds)
            Result.Success(analysis)
        }
        val viewModel = subject()

        repeat(3) { viewModel.onAction(AnalyzeAction.Analyze) }
        advanceUntilIdle()

        coVerify(exactly = 1) { analyzer.analyze(any()) }
        assertTrue(viewModel.state.value is AnalyzeState.Success)
    }

    @Test
    fun `실패한 뒤 재시도하면 다시 분석한다`() = runTest {
        givenError(AnalysisError.Network)
        val viewModel = subject()
        advanceUntilIdle()
        assertEquals(AnalyzeState.Error(AnalysisError.Network), viewModel.state.value)

        coEvery { analyzer.analyze(any()) } returns Result.Success(analysis)
        viewModel.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()

        coVerify(exactly = 2) { analyzer.analyze(any()) }
        assertTrue(viewModel.state.value is AnalyzeState.Success)
    }

    @Test
    fun `재시도하면 Loading 을 다시 거친다`() = runTest {
        givenError(AnalysisError.Network)
        val viewModel = subject()
        val states = collectStates(viewModel)
        advanceUntilIdle()

        coEvery { analyzer.analyze(any()) } returns Result.Success(analysis)
        viewModel.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()

        // Loading -> Error -> Loading -> Success
        assertEquals(4, states.size)
        assertEquals(AnalyzeState.Loading, states[2])
        assertTrue(states[3] is AnalyzeState.Success)
    }

    @Test
    fun `분석기에 비어있지 않은 사진이 전달된다`() = runTest {
        val captured = slot<PlantPhoto>()
        subject()

        advanceUntilIdle()

        coVerify { analyzer.analyze(capture(captured)) }
        assertTrue(captured.captured.bytes.isNotEmpty())
    }
}
