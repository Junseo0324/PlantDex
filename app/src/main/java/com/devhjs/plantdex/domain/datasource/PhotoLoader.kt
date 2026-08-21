package com.devhjs.plantdex.domain.datasource

import com.devhjs.plantdex.domain.model.PlantPhoto

/**
 * 사진 위치에서 분석에 넘길 바이트를 읽는 포트.
 */
interface PhotoLoader {
    /** 읽지 못하면 null. */
    suspend fun load(photoUri: String): PlantPhoto?
}
