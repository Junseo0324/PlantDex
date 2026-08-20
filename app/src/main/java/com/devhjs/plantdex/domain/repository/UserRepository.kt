package com.devhjs.plantdex.domain.repository

import com.devhjs.plantdex.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * 사용자 정보 포트
 * 로그인이 붙기 전까지는 목업 한 명만 흘린다.
 */
interface UserRepository {

    fun observeProfile(): Flow<UserProfile>
}
