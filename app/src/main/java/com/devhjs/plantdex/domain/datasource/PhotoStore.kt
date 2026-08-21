package com.devhjs.plantdex.domain.datasource

/**
 * 도감이 참조할 사진을 앱 저장소에 보관하는 포트.
 *
 * 갤러리가 준 위치는 프로세스가 끝나면 읽을 수 없어 반드시 복사해 둬야 한다.
 */
interface PhotoStore {
    /** 복사한 사진의 위치. 실패하면 null. */
    suspend fun save(sourceUri: String): String?

    /** 보관 중인 사진만 지운다. */
    suspend fun delete(photoUri: String)
}
