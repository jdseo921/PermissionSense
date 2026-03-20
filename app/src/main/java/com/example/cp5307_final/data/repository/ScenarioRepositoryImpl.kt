package com.example.cp5307_final.data.repository

import com.example.cp5307_final.data.local.dao.ScenarioDao
import com.example.cp5307_final.data.local.dao.UserProgressDao
import com.example.cp5307_final.data.local.SampleData
import com.example.cp5307_final.data.mapper.toDomain
import com.example.cp5307_final.data.mapper.toEntity
import com.example.cp5307_final.data.remote.ApiService
import com.example.cp5307_final.domain.model.CategoryStats
import com.example.cp5307_final.domain.model.Scenario
import com.example.cp5307_final.domain.model.Statistics
import com.example.cp5307_final.domain.model.UserProgress
import com.example.cp5307_final.domain.repository.ScenarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScenarioRepositoryImpl @Inject constructor(
    private val scenarioDao: ScenarioDao,
    private val userProgressDao: UserProgressDao,
    private val apiService: ApiService
) : ScenarioRepository {

    override fun getScenarios(): Flow<List<Scenario>> {
        return scenarioDao.getAllScenarios().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getScenarioById(id: Int): Scenario? {
        return scenarioDao.getScenarioById(id)?.toDomain()
    }

    override suspend fun saveProgress(progress: UserProgress) {
        userProgressDao.saveProgress(progress.toEntity())
    }

    override fun getCompletedCount(): Flow<Int> {
        return userProgressDao.getCompletedCount()
    }

    override fun getAccuracy(): Flow<Float> {
        return combine(
            userProgressDao.getCompletedCount(),
            userProgressDao.getCorrectCount()
        ) { completed, correct ->
            if (completed == 0) 0f else (correct.toFloat() / completed.toFloat()) * 100f
        }
    }

    override fun getStatistics(): Flow<Statistics> {
        return combine(
            scenarioDao.getAllScenarios(),
            userProgressDao.getAllProgress()
        ) { scenarios, progressList ->
            val progressMap = progressList.associateBy { it.scenarioId }
            val completedCount = progressList.count { it.completed }
            val correctCount = progressList.count { it.isCorrect }
            
            val categoryStats = scenarios.groupBy { it.category }.mapValues { (category, categoryScenarios) ->
                val totalInCategory = categoryScenarios.size
                val completedInCategory = categoryScenarios.count { progressMap[it.id]?.completed == true }
                val correctInCategory = categoryScenarios.count { progressMap[it.id]?.isCorrect == true }
                CategoryStats(totalInCategory, completedInCategory, correctInCategory)
            }

            Statistics(
                totalScenarios = scenarios.size,
                completedScenarios = completedCount,
                correctAnswers = correctCount,
                averageScore = if (completedCount == 0) 0f else (correctCount.toFloat() / completedCount.toFloat()) * 100f,
                categoryBreakdown = categoryStats
            )
        }
    }

    override suspend fun refreshScenarios() {
        try {
            val remoteScenarios = apiService.getScenarios()
            if (remoteScenarios.isNotEmpty()) {
                scenarioDao.deleteAllScenarios()
                scenarioDao.insertScenarios(remoteScenarios.map { it.toEntity() })
            }
        } catch (e: Exception) {
            // Fallback to local sample data if DB is empty and network fails
            val currentScenarios = scenarioDao.getScenarioById(1) // Check if any exist
            if (currentScenarios == null) {
                scenarioDao.insertScenarios(SampleData.scenarios)
            }
        }
    }
}
