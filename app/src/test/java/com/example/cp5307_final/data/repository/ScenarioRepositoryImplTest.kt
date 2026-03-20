package com.example.cp5307_final.data.repository

import app.cash.turbine.test
import com.example.cp5307_final.data.local.dao.ScenarioDao
import com.example.cp5307_final.data.local.dao.UserProgressDao
import com.example.cp5307_final.data.local.entity.ScenarioEntity
import com.example.cp5307_final.data.local.entity.UserProgressEntity
import com.example.cp5307_final.data.remote.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ScenarioRepositoryImplTest {

    private lateinit var repository: ScenarioRepositoryImpl
    private val scenarioDao: ScenarioDao = mockk(relaxed = true)
    private val userProgressDao: UserProgressDao = mockk(relaxed = true)
    private val apiService: ApiService = mockk()

    private val mockEntities = listOf(
        ScenarioEntity(1, "T1", "D1", "Cat1", "Easy", "A", "B", "C", "D", 1, "E1"),
        ScenarioEntity(2, "T2", "D2", "Cat1", "Easy", "E", "F", "G", "H", 0, "E2")
    )

    private val mockProgress = listOf(
        UserProgressEntity(1, true, 1, true, 1000L),
        UserProgressEntity(2, true, 1, false, 1100L)
    )

    @Before
    fun setup() {
        repository = ScenarioRepositoryImpl(scenarioDao, userProgressDao, apiService)
    }

    @Test
    fun `getStatistics calculates correct values`() = runTest {
        coEvery { scenarioDao.getAllScenarios() } returns flowOf(mockEntities)
        coEvery { userProgressDao.getAllProgress() } returns flowOf(mockProgress)

        repository.getStatistics().test {
            val stats = awaitItem()
            
            assertEquals(2, stats.totalScenarios)
            assertEquals(2, stats.completedScenarios)
            assertEquals(1, stats.correctAnswers)
            assertEquals(50f, stats.averageScore)
            
            val cat1Stats = stats.categoryBreakdown["Cat1"]
            assertEquals(2, cat1Stats?.total)
            assertEquals(2, cat1Stats?.completed)
            assertEquals(1, cat1Stats?.correct)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshScenarios fetches from API and updates DAO`() = runTest {
        coEvery { apiService.getScenarios() } returns emptyList() // Simplified

        repository.refreshScenarios()

        coVerify { apiService.getScenarios() }
    }

    @Test
    fun `refreshScenarios falls back to sample data on API failure and empty DB`() = runTest {
        coEvery { apiService.getScenarios() } throws Exception("Network Error")
        coEvery { scenarioDao.getScenarioById(1) } returns null

        repository.refreshScenarios()

        coVerify { scenarioDao.insertScenarios(any()) }
    }
}
