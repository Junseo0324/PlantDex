package com.devhjs.plantdex.domain.model

/**
 * 도감에 등록된 식물.
 *
 * [id] - 발견 순서대로 1 부터 부여된다.
 * [photoUri] - 촬영 사진의 위치. 아직 카메라가 붙지 않아 현재는 항상 null 이고, 화면은 플레이스홀더로 대체한다.
 */
data class DexEntry(
    val id: Long,
    val dexNumber: Int,
    val plant: Plant,
    val photoUri: String? = null,
    val isFavorite: Boolean = false,
    val memo: String? = null,
)
