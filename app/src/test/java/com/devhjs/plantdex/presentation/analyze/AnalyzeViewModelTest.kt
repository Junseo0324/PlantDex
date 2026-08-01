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

@OptIn(ExperimentalCoroutinesApi::class)
class AnalyzeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val analyzer = mockk<PlantAnalyzer>()
    private val clock = mockk<Clock> {
        every { now() } returns Instant.fromEpochMilliseconds(1_700_000_000_000)
    }

    private val subject = AnalyzeViewModel(AnalyzePlantPhotoUseCase(analyzer, clock))

    private val analysis = PlantAnalysis(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "열대 관엽식물",
        origin = "멕시코 남부",
        watering = "2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        rawDifficulty = 2,
    )

    private fun givenSuccess() {
        coEvery { analyzer.analyze(any()) } returns Result.Success(analysis)
    }

    private fun givenError(error: AnalysisError) {
        coEvery { analyzer.analyze(any()) } returns Result.Error(error)
    }

    private fun TestScope.collectStates(): List<AnalyzeState> {
        val states = mutableListOf<AnalyzeState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            subject.state.toList(states)
        }
        return states
    }

    @Test
    fun `초기 상태는 Idle 이다`() = runTest {
        assertEquals(AnalyzeState.Idle, subject.state.value)
    }

    @Test
    fun `성공하면 Idle Loading Success 순서로 바뀐다`() = runTest {
        givenSuccess()
        val states = collectStates()

        subject.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()

        assertEquals(3, states.size)
        assertEquals(AnalyzeState.Idle, states[0])
        assertEquals(AnalyzeState.Loading, states[1])
        assertTrue(states[2] is AnalyzeState.Success)
        assertEquals("몬스테라", (states[2] as AnalyzeState.Success).plant.name)
    }

    @Test
    fun `실패하면 Idle Loading Error 순서로 바뀌고 에러 종류가 유지된다`() = runTest {
        givenError(AnalysisError.NotAPlant)
        val states = collectStates()

        subject.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()

        assertEquals(3, states.size)
        assertEquals(AnalyzeState.Loading, states[1])
        assertEquals(AnalyzeState.Error(AnalysisError.NotAPlant), states[2])
    }

    @Test
    fun `발견일이 결과에 반영된다`() = runTest {
        val now = Instant.fromEpochMilliseconds(1_234_567_890)
        every { clock.now() } returns now
        givenSuccess()

        subject.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()

        assertEquals(now, (subject.state.value as AnalyzeState.Success).plant.discoveredAt)
    }

    @Test
    fun `분석 중 연타하면 한 번만 실행된다`() = runTest {
        coEvery { analyzer.analyze(any()) } coAnswers {
            delay(500.milliseconds)
            Result.Success(analysis)
        }

        subject.onAction(AnalyzeAction.Analyze)
        subject.onAction(AnalyzeAction.Analyze)
        subject.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()

        coVerify(exactly = 1) { analyzer.analyze(any()) }
        assertTrue(subject.state.value is AnalyzeState.Success)
    }

    @Test
    fun `Success 에서 Reset 액션을 주면 Idle 로 돌아간다`() = runTest {
        givenSuccess()

        subject.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()
        assertTrue(subject.state.value is AnalyzeState.Success)

        subject.onAction(AnalyzeAction.Reset)

        assertEquals(AnalyzeState.Idle, subject.state.value)
    }

    @Test
    fun `결과가 나온 뒤에도 다시 분석할 수 있다`() = runTest {
        givenSuccess()
        subject.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()

        givenError(AnalysisError.Network)
        subject.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()

        coVerify(exactly = 2) { analyzer.analyze(any()) }
        assertEquals(AnalyzeState.Error(AnalysisError.Network), subject.state.value)
    }

    @Test
    fun `분석기에 비어있지 않은 사진이 전달된다`() = runTest {
        givenSuccess()
        val captured = slot<PlantPhoto>()

        subject.onAction(AnalyzeAction.Analyze)
        advanceUntilIdle()

        coVerify { analyzer.analyze(capture(captured)) }
        assertTrue(captured.captured.bytes.isNotEmpty())
    }
}
