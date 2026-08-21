package com.devhjs.plantdex.domain.model

import androidx.compose.runtime.Stable

/**
 * 도감에 등록된 식물.
 *
 * [dexNumber] - 발견 순서대로 1 부터 부여된다.
 * [photoUri] - 촬영 사진의 위치. 파일이 사라지면 화면은 플레이스홀더로 대체한다.
 */
@Stable
data class DexEntry(
    val id: Long,
    val dexNumber: Int,
    val plant: Plant,
    val photoUri: String? = null,
    val isFavorite: Boolean = false,
    val memo: String? = null,
)
