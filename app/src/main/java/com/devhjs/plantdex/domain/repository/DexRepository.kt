package com.devhjs.plantdex.domain.repository

import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.Plant
import kotlinx.coroutines.flow.Flow

/**
 * 도감 저장소 포트
 * 추후 Room 구현 추가 예정
 *
 */
interface DexRepository {

    fun observeAll(): Flow<List<DexEntry>>

    /** 해당 id 의 항목. 삭제됐거나 없으면 null 을 흘린다. */
    fun observe(id: Long): Flow<DexEntry?>

    /** 분석 결과를 도감에 등록하고 다음 도감 번호 부여 */
    suspend fun register(plant: Plant, photoUri: String? = null): DexEntry

    suspend fun setFavorite(id: Long, favorite: Boolean)

    suspend fun setMemo(id: Long, memo: String)
}
