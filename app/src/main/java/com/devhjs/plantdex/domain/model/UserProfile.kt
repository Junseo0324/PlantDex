package com.devhjs.plantdex.domain.model

import kotlin.time.Instant

/**
 * [avatarUri] - 아직 사진을 고를 수 없어 항상 null 이고, 화면은 플레이스홀더로 대체한다.
 */
data class UserProfile(
    val name: String,
    val joinedAt: Instant,
    val avatarUri: String? = null,
)
