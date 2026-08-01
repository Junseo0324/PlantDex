package com.devhjs.plantdex.core.di

import com.devhjs.plantdex.data.datasource.MockPlantAnalyzer
import com.devhjs.plantdex.domain.datasource.PlantAnalyzer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 실제 AI 프로바이더가 정해지면 여기 바인딩만 갈아끼우면 된다.
 */
@Module가
@InstallIn(SingletonComponent::class)
abstract class AnalysisModule {

    @Binds
    abstract fun bindPlantAnalyzer(analyzer: MockPlantAnalyzer): PlantAnalyzer
}
