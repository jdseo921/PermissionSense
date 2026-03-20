package com.example.cp5307_final.di

import android.content.Context
import androidx.room.Room
import com.example.cp5307_final.data.local.AppDatabase
import com.example.cp5307_final.data.local.dao.ScenarioDao
import com.example.cp5307_final.data.local.dao.UserProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "permissionsense.db"
        ).build()
    }

    @Provides
    fun provideScenarioDao(db: AppDatabase): ScenarioDao = db.scenarioDao()

    @Provides
    fun provideUserProgressDao(db: AppDatabase): UserProgressDao = db.userProgressDao()
}
