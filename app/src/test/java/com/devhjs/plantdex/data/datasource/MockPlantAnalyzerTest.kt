package com.devhjs.plantdex.data.datasource

import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.domain.model.PhotoFormat
import com.devhjs.plantdex.domain.model.PlantPhoto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MockPlantAnalyzerTest {

    private val photo = PlantPhoto(ByteArray(8), PhotoFormat.JPEG)

    @Test
    fun `canned 식물을 성공으로 반환한다`() = runTest {
        val result = MockPlantAnalyzer().analyze(photo)

        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data in MockPlants.All)
    }

    @Test
    fun `canned 데이터의 필드가 비어있지 않다`() {
        MockPlants.All.forEach { entry ->
            assertTrue("name 비어있음: $entry", entry.name.isNotBlank())
            assertTrue("englishName 비어있음: $entry", entry.englishName.isNotBlank())
            assertTrue("description 비어있음: $entry", entry.description.isNotBlank())
            assertTrue("origin 비어있음: $entry", entry.origin.isNotBlank())
            assertTrue("watering 비어있음: $entry", entry.watering.isNotBlank())
        }
    }

    @Test
    fun `클램프 확인용으로 범위를 벗어난 난이도가 남아있다`() {
        assertEquals(7, MockPlants.Stuckyi.rawDifficulty)
        assertTrue(MockPlants.All.any { it.rawDifficulty !in 1..5 })
    }
}
