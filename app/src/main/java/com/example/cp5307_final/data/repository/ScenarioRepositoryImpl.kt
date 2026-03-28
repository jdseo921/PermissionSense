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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.concurrent.TimeUnit
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
            if (entities.isEmpty()) {
                // If database is empty, seed it with SampleData
                scenarioDao.insertScenarios(SampleData.scenarios)
                SampleData.scenarios.map { it.toDomain() }
            } else {
                entities.map { it.toDomain() }
            }
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
                currentStreak = calculateStreak(progressList.map { it.lastAttemptedAt }),
                categoryBreakdown = categoryStats
            )
        }
    }

    override fun getMissedScenarios(): Flow<List<Scenario>> {
        return combine(
            scenarioDao.getAllScenarios(),
            userProgressDao.getAllProgress()
        ) { scenarios, progressList ->
            val missedIds = progressList.filter { !it.isCorrect && it.completed }.map { it.scenarioId }.toSet()
            scenarios.filter { it.id in missedIds }.map { it.toDomain() }
        }
    }

    private fun calculateStreak(timestamps: List<Long>): Int {
        if (timestamps.isEmpty()) return 0
        
        val sortedDates = timestamps.map { 
            val cal = Calendar.getInstance()
            cal.timeInMillis = it
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val ms = cal.timeInMillis
            ms
        }.distinct().sortedDescending()

        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        val todayMs = today.timeInMillis

        var streak = 0
        var expectedDate = todayMs

        if (sortedDates.isEmpty()) return 0

        if (sortedDates.first() < todayMs - TimeUnit.DAYS.toMillis(1)) {
            return 0
        }

        expectedDate = sortedDates.first()
        for (date in sortedDates) {
            if (date == expectedDate) {
                streak++
                expectedDate -= TimeUnit.DAYS.toMillis(1)
            } else {
                break
            }
        }

        return streak
    }

    override suspend fun refreshScenarios() {
        try {
            val remoteScenarios = apiService.getScenarios()
            if (remoteScenarios.isNotEmpty()) {
                scenarioDao.deleteAllScenarios()
                scenarioDao.insertScenarios(remoteScenarios.map { it.toEntity() })
            }
        } catch (e: Exception) {
            val currentScenarios = scenarioDao.getAllScenarios().map { it.isNotEmpty() }.firstOrNull()
            if (currentScenarios != true) {
                scenarioDao.insertScenarios(SampleData.scenarios)
            }
        }
    }

    override suspend fun resetUserProgress() {
        userProgressDao.deleteAllProgress()
    }
}
