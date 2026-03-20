package com.example.cp5307_final.di

import com.example.cp5307_final.data.repository.ScenarioRepositoryImpl
import com.example.cp5307_final.domain.repository.ScenarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindScenarioRepository(
        repository: ScenarioRepositoryImpl
    ): ScenarioRepository
}
