package com.devhjs.plantdex.core.di

import com.devhjs.plantdex.data.repository.MockDexRepositoryImpl
import com.devhjs.plantdex.domain.repository.DexRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Room 구현이 생기면 여기 바인딩만 갈아끼우면 된다. -> Flavor 이용 예정
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDexRepository(repository: MockDexRepositoryImpl): DexRepository
}
