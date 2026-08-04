package com.devhjs.plantdex.data.repository

import com.devhjs.plantdex.data.datasource.MockPlants
import com.devhjs.plantdex.domain.mapper.toPlant
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import com.devhjs.plantdex.domain.repository.DexRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

/**
 * Room 이 붙기 전까지 쓰는 인메모리 도감. 앱을 끄면 사라진다.
 *
 * 빈 화면부터 시작하지 않도록 MockPlants 로 시드를 넣는다.
 */
@Singleton
class MockDexRepositoryImpl @Inject constructor(
    private val clock: Clock,
) : DexRepository {

    private val entries = MutableStateFlow(seed())

    /** 읽기-고치기-쓰기가 겹치지 않기 위한 Mutex 사용 */
    private val writeLock = Mutex()

    override fun observeAll(): Flow<List<DexEntry>> =
        entries.map { list -> list.sortedByDescending(DexEntry::dexNumber) }

    override fun observe(id: Long): Flow<DexEntry?> =
        entries.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun register(plant: Plant, photoUri: String?): DexEntry = writeLock.withLock {
        val current = entries.value
        val nextNumber = (current.maxOfOrNull(DexEntry::dexNumber) ?: 0) + 1
        val entry = DexEntry(
            id = (current.maxOfOrNull(DexEntry::id) ?: 0L) + 1L,
            dexNumber = nextNumber,
            plant = plant,
            photoUri = photoUri,
        )
        entries.value = current + entry
        entry
    }

    override suspend fun setFavorite(id: Long, favorite: Boolean) =
        edit(id) { it.copy(isFavorite = favorite) }

    override suspend fun setMemo(id: Long, memo: String) =
        edit(id) { it.copy(memo = memo) }

    private suspend fun edit(id: Long, transform: (DexEntry) -> DexEntry) = writeLock.withLock {
        entries.value = entries.value.map { if (it.id == id) transform(it) else it }
    }


    private fun seed(): List<DexEntry> {
        val now = clock.now()
        val seeds = MockPlants.All.zip(SEED_AGES)
        return seeds.mapIndexed { index, (analysis, age) ->
            val number = seeds.size - index
            DexEntry(
                id = number.toLong(),
                dexNumber = number,
                plant = analysis.toPlant(discoveredAt = now - age),
            )
        }
    }

    private companion object {
        val SEED_AGES = listOf(2.days, 10.days, 45.days, 90.days)
    }
}
