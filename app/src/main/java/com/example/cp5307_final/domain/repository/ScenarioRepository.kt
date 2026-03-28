package com.example.cp5307_final.domain.repository

import com.example.cp5307_final.domain.model.Scenario
import com.example.cp5307_final.domain.model.Statistics
import com.example.cp5307_final.domain.model.UserProgress
import kotlinx.coroutines.flow.Flow

interface ScenarioRepository {
    fun getScenarios(): Flow<List<Scenario>>
    suspend fun getScenarioById(id: Int): Scenario?
    suspend fun saveProgress(progress: UserProgress)
    fun getCompletedCount(): Flow<Int>
    fun getAccuracy(): Flow<Float>
    fun getStatistics(): Flow<Statistics>
    fun getMissedScenarios(): Flow<List<Scenario>>
    suspend fun refreshScenarios()
    suspend fun resetUserProgress()
}
