package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.DexCollection
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
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class GetDexCollectionUseCaseTest {

    private val now = Instant.fromEpochMilliseconds(1_785_000_000_000)

    private val entries = MutableStateFlow<List<DexEntry>>(emptyList())
    private val repository = mockk<DexRepository> {
        every { observeAll() } returns entries.map { list ->
            list.sortedByDescending(DexEntry::dexNumber)
        }
    }

    private val subject = GetDexCollectionUseCase(repository)

    private fun entry(
        number: Int,
        name: String,
        englishName: String = "Monstera deliciosa",
        isFavorite: Boolean = false,
        daysAgo: Int = 1,
    ) = DexEntry(
        id = number.toLong(),
        dexNumber = number,
        plant = Plant(
            name = name,
            englishName = englishName,
            description = "열대 관엽식물",
            origin = "멕시코 남부",
            watering = "2주에 한 번",
            sunlight = Sunlight.BRIGHT_INDIRECT,
            difficulty = 2,
            discoveredAt = now - daysAgo.days,
        ),
        isFavorite = isFavorite,
    )

    private suspend fun collect(
        query: String = "",
        favoritesOnly: Boolean = false,
    ): DexCollection = subject(query, favoritesOnly).first()

    private fun givenSample() {
        entries.value = listOf(
            entry(1, "몬스테라", "Monstera deliciosa", daysAgo = 30),
            entry(2, "산세베리아", "Sansevieria trifasciata", isFavorite = true, daysAgo = 10),
            entry(3, "스투키", "Sansevieria stuckyi", daysAgo = 3),
            entry(4, "아디안텀", "Adiantum raddianum", isFavorite = true, daysAgo = 1),
        )
    }

    @Test
    fun `도감이 비면 기본값이다`() = runTest {
        assertEquals(DexCollection(), collect())
    }

    @Test
    fun `검색어가 비면 전부 통과한다`() = runTest {
        givenSample()

        assertEquals(4, collect().entries.size)
    }

    @Test
    fun `이름 부분 일치로 걸러진다`() = runTest {
        givenSample()

        assertEquals(listOf("몬스테라"), collect(query = "몬스").entries.map { it.plant.name })
    }

    @Test
    fun `학명 부분 일치로도 걸러진다`() = runTest {
        givenSample()

        val names = collect(query = "Sansevieria").entries.map { it.plant.name }

        assertEquals(listOf("스투키", "산세베리아"), names)
    }

    @Test
    fun `대소문자를 무시한다`() = runTest {
        givenSample()

        assertEquals(1, collect(query = "adiantum").entries.size)
        assertEquals(1, collect(query = "ADIANTUM").entries.size)
    }

    @Test
    fun `검색어 앞뒤 공백은 무시한다`() = runTest {
        givenSample()

        assertEquals(listOf("몬스테라"), collect(query = "  몬스테라  ").entries.map { it.plant.name })
    }

    @Test
    fun `공백만 있는 검색어는 전체로 취급한다`() = runTest {
        givenSample()

        assertEquals(4, collect(query = "   ").entries.size)
    }

    @Test
    fun `즐겨찾기 필터가 적용된다`() = runTest {
        givenSample()

        val names = collect(favoritesOnly = true).entries.map { it.plant.name }

        assertEquals(listOf("아디안텀", "산세베리아"), names)
    }

    @Test
    fun `검색과 즐겨찾기가 함께 적용된다`() = runTest {
        givenSample()

        val names = collect(query = "Sansevieria", favoritesOnly = true).entries.map { it.plant.name }

        assertEquals(listOf("산세베리아"), names)
    }

    @Test
    fun `결과가 0개여도 totalCount 는 전체 개수다`() = runTest {
        givenSample()

        val collection = collect(query = "존재하지않는식물")

        assertEquals(0, collection.entries.size)
        assertEquals(4, collection.totalCount)
    }

    @Test
    fun `즐겨찾기만 볼 때도 totalCount 는 전체 개수다`() = runTest {
        givenSample()

        val collection = collect(favoritesOnly = true)

        assertEquals(2, collection.entries.size)
        assertEquals(4, collection.totalCount)
    }

    @Test
    fun `lastDiscoveredAt 은 필터와 무관하게 가장 최근이다`() = runTest {
        givenSample()

        assertEquals(now - 1.days, collect().lastDiscoveredAt)
        assertEquals(now - 1.days, collect(query = "몬스테라").lastDiscoveredAt)
    }

    @Test
    fun `도감이 비면 lastDiscoveredAt 은 null 이다`() = runTest {
        assertNull(collect().lastDiscoveredAt)
    }

    @Test
    fun `저장소 정렬(도감 번호 내림차순)이 유지된다`() = runTest {
        givenSample()

        assertEquals(listOf(4, 3, 2, 1), collect().entries.map(DexEntry::dexNumber))
    }

    @Test
    fun `도감이 바뀌면 다시 흘러나온다`() = runTest {
        givenSample()
        assertEquals(4, collect().totalCount)

        entries.value = entries.value + entry(5, "튤립", "Tulipa gesneriana")

        assertEquals(5, collect().totalCount)
    }
}
