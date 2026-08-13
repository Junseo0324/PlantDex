가package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.model.Sunlight
import com.devhjs.plantdex.domain.repository.DexRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.time.Instant

class ObserveDexEntryUseCaseTest {

    private val entry = DexEntry(
        id = 4L,
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
    )

    private val observed = MutableStateFlow<DexEntry?>(null)
    private val repository = mockk<DexRepository> {
        every { observe(any()) } returns observed
    }

    private val subject = ObserveDexEntryUseCase(repository)

    @Test
    fun `저장소가 흘린 항목을 그대로 전달한다`() = runTest {
        observed.value = entry

        assertEquals(entry, subject(4L).first())
    }

    @Test
    fun `없는 항목이면 null 을 전달한다`() = runTest {
        assertNull(subject(-1L).first())
    }

    @Test
    fun `요청한 id 를 그대로 저장소에 넘긴다`() = runTest {
        subject(7L).first()

        verify(exactly = 1) { repository.observe(7L) }
    }

    @Test
    fun `전체 목록이 아니라 단건 조회를 쓴다`() = runTest {
        subject(4L).first()

        verify(exactly = 0) { repository.observeAll() }
    }

    @Test
    fun `저장소 값이 바뀌면 이어서 흘린다`() = runTest {
        observed.value = entry
        assertEquals(entry, subject(4L).first())

        val renamed = entry.copy(isFavorite = true)
        observed.value = renamed

        assertEquals(renamed, subject(4L).first())
    }
}
