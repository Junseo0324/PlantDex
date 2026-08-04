package com.devhjs.plantdex.data.repository

import com.devhjs.plantdex.data.datasource.MockPlants
import com.devhjs.plantdex.domain.mapper.toPlant
import com.devhjs.plantdex.domain.model.DexEntry
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Instant

class MockDexRepositoryImplTest {

    private val now = Instant.fromEpochMilliseconds(1_785_000_000_000)
    private val clock = mockk<Clock> { every { now() } returns now }

    private val subject = MockDexRepositoryImpl(clock)

    private val newPlant = MockPlants.Monstera.toPlant(discoveredAt = now)

    private suspend fun all(): List<DexEntry> = subject.observeAll().first()

    @Test
    fun `시드가 들어있고 도감 번호는 1부터 빠짐없이 부여된다`() = runTest {
        val entries = all()

        assertEquals(MockPlants.All.size, entries.size)
        assertEquals((1..MockPlants.All.size).toList(), entries.map(DexEntry::dexNumber).sorted())
    }

    @Test
    fun `observeAll 은 도감 번호 내림차순으로 흘린다`() = runTest {
        val numbers = all().map(DexEntry::dexNumber)

        assertEquals(numbers.sortedDescending(), numbers)
    }

    @Test
    fun `오래 발견한 항목일수록 낮은 번호를 갖는다`() = runTest {
        val entries = all()

        val byDiscovery = entries.sortedBy { it.plant.discoveredAt }.map(DexEntry::dexNumber)
        assertEquals(byDiscovery.sorted(), byDiscovery)
    }

    @Test
    fun `register 는 다음 도감 번호를 부여한다`() = runTest {
        val before = all().maxOf(DexEntry::dexNumber)

        val registered = subject.register(newPlant)

        assertEquals(before + 1, registered.dexNumber)
    }

    @Test
    fun `register 한 항목이 목록 맨 앞에 들어간다`() = runTest {
        val registered = subject.register(newPlant)

        val entries = all()
        assertEquals(MockPlants.All.size + 1, entries.size)
        assertEquals(registered, entries.first())
    }

    @Test
    fun `register 를 반복해도 번호와 id 가 겹치지 않는다`() = runTest {
        repeat(3) { subject.register(newPlant) }

        val entries = all()
        assertEquals(entries.size, entries.map(DexEntry::dexNumber).distinct().size)
        assertEquals(entries.size, entries.map(DexEntry::id).distinct().size)
    }

    @Test
    fun `register 에 넘긴 사진 위치가 보존된다`() = runTest {
        val registered = subject.register(newPlant, photoUri = "content://photo/1")

        assertEquals("content://photo/1", subject.observe(registered.id).first()?.photoUri)
    }

    @Test
    fun `observe 는 해당 id 의 항목을 흘린다`() = runTest {
        val target = all().first()

        assertEquals(target, subject.observe(target.id).first())
    }

    @Test
    fun `없는 id 를 observe 하면 null 이다`() = runTest {
        assertNull(subject.observe(-1L).first())
    }

    @Test
    fun `setFavorite 이 해당 항목에만 반영된다`() = runTest {
        val target = all().first()

        subject.setFavorite(target.id, favorite = true)

        val entries = all()
        assertTrue(entries.first { it.id == target.id }.isFavorite)
        assertTrue(entries.filter { it.id != target.id }.none(DexEntry::isFavorite))
    }

    @Test
    fun `setMemo 가 반영된다`() = runTest {
        val target = all().first()

        subject.setMemo(target.id, "창가 화분")

        assertEquals("창가 화분", subject.observe(target.id).first()?.memo)
    }

    @Test
    fun `없는 id 를 수정해도 목록이 그대로다`() = runTest {
        val before = all()

        subject.setFavorite(-1L, favorite = true)
        subject.setMemo(-1L, "무시된다")

        assertEquals(before, all())
    }
}
