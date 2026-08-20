package com.devhjs.plantdex.data.repository

import com.devhjs.plantdex.domain.model.UserProfile
import com.devhjs.plantdex.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Instant

/**
 * 로그인이 붙기 전까지 쓰는 고정 사용자.
 */
@Singleton
class MockUserRepositoryImpl @Inject constructor() : UserRepository {

    override fun observeProfile(): Flow<UserProfile> = flowOf(MOCK_USER)

    private companion object {
        /** 2026.03.02 */
        val MOCK_USER = UserProfile(
            name = "홍길동",
            joinedAt = Instant.fromEpochMilliseconds(1_772_409_600_000),
        )
    }
}
