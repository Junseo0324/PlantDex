package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.domain.datasource.PlantAnalyzer
import com.devhjs.plantdex.domain.model.AnalysisError
import com.devhjs.plantdex.domain.model.PhotoFormat
import com.devhjs.plantdex.domain.model.PlantAnalysis
import com.devhjs.plantdex.domain.model.PlantPhoto
import com.devhjs.plantdex.domain.model.Sunlight
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Instant

class AnalyzePlantPhotoUseCaseTest {

    private val photo = PlantPhoto(ByteArray(8), PhotoFormat.JPEG)
    private val analyzer = mockk<PlantAnalyzer>()
    private val clock = mockk<Clock>()

    private val subject = AnalyzePlantPhotoUseCase(analyzer, clock)

    private fun analysis(rawDifficulty: Int = 3) = PlantAnalysis(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "열대 관엽식물",
        origin = "멕시코 남부",
        watering = "2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        rawDifficulty = rawDifficulty,
    )

    private fun givenAnalysis(analysis: PlantAnalysis) {
        coEvery { analyzer.analyze(any()) } returns Result.Success(analysis)
    }

    private fun givenError(error: AnalysisError) {
        coEvery { analyzer.analyze(any()) } returns Result.Error(error)
    }

    private fun givenNow(instant: Instant = Instant.fromEpochMilliseconds(0)) {
        every { clock.now() } returns instant
    }

    private suspend fun analyzeWithDifficulty(rawDifficulty: Int): Int {
        givenAnalysis(analysis(rawDifficulty))
        givenNow()
        return (subject(photo) as Result.Success).data.difficulty
    }

    @Test
    fun `난이도가 최소값보다 작으면 1로 클램프된다`() = runTest {
        assertEquals(1, analyzeWithDifficulty(0))
        assertEquals(1, analyzeWithDifficulty(-3))
    }

    @Test
    fun `난이도가 최대값보다 크면 5로 클램프된다`() = runTest {
        assertEquals(5, analyzeWithDifficulty(7))
        assertEquals(5, analyzeWithDifficulty(100))
    }

    @Test
    fun `범위 안의 난이도는 그대로 통과한다`() = runTest {
        assertEquals(1, analyzeWithDifficulty(1))
        assertEquals(3, analyzeWithDifficulty(3))
        assertEquals(5, analyzeWithDifficulty(5))
    }

    @Test
    fun `발견일은 주입된 시계에서 찍힌다`() = runTest {
        val now = Instant.fromEpochMilliseconds(1_700_000_000_000)
        givenAnalysis(analysis())
        givenNow(now)

        val result = subject(photo)

        assertEquals(now, (result as Result.Success).data.discoveredAt)
    }

    @Test
    fun `시계가 흐르면 발견일도 달라진다`() = runTest {
        givenAnalysis(analysis())
        every { clock.now() } returnsMany listOf(
            Instant.fromEpochMilliseconds(1_000),
            Instant.fromEpochMilliseconds(2_000),
        )

        val first = (subject(photo) as Result.Success).data.discoveredAt
        val second = (subject(photo) as Result.Success).data.discoveredAt

        assertEquals(Instant.fromEpochMilliseconds(1_000), first)
        assertEquals(Instant.fromEpochMilliseconds(2_000), second)
    }

    @Test
    fun `실패는 그대로 전달되고 Plant 를 만들지 않는다`() = runTest {
        givenError(AnalysisError.Network)

        val result = subject(photo)

        assertTrue(result is Result.Error)
        assertEquals(AnalysisError.Network, (result as Result.Error).error)
        verify(exactly = 0) { clock.now() }
    }

    @Test
    fun `실패 종류가 뭉개지지 않는다`() = runTest {
        givenError(AnalysisError.NotAPlant)

        val result = subject(photo)

        assertEquals(AnalysisError.NotAPlant, (result as Result.Error).error)
    }

    @Test
    fun `문자열 필드의 앞뒤 공백은 제거된다`() = runTest {
        givenAnalysis(
            analysis().copy(
                name = "  몬스테라 ",
                englishName = " Monstera deliciosa  ",
                description = "\n열대 관엽식물\t",
                origin = "  멕시코 남부",
                watering = "2주에 한 번   ",
            )
        )
        givenNow()

        val plant = (subject(photo) as Result.Success).data

        assertEquals("몬스테라", plant.name)
        assertEquals("Monstera deliciosa", plant.englishName)
        assertEquals("열대 관엽식물", plant.description)
        assertEquals("멕시코 남부", plant.origin)
        assertEquals("2주에 한 번", plant.watering)
    }

    @Test
    fun `사진이 분석기에 그대로 전달된다`() = runTest {
        givenAnalysis(analysis())
        givenNow()

        subject(photo)

        coVerify(exactly = 1) { analyzer.analyze(photo) }
    }
}
