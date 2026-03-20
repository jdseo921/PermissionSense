package com.example.cp5307_final.ui.activity

import app.cash.turbine.test
import com.example.cp5307_final.domain.model.Scenario
import com.example.cp5307_final.domain.repository.ScenarioRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActivityViewModelTest {

    private lateinit var viewModel: ActivityViewModel
    private val repository: ScenarioRepository = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val mockScenarios = listOf(
        Scenario(
            id = 1,
            title = "Test 1",
            scenarioText = "Description 1",
            category = "Cat 1",
            difficulty = "Easy",
            choices = listOf("A", "B", "C", "D"),
            correctAnswerIndex = 1,
            explanation = "Exp 1"
        ),
        Scenario(
            id = 2,
            title = "Test 2",
            scenarioText = "Description 2",
            category = "Cat 2",
            difficulty = "Medium",
            choices = listOf("E", "F", "G", "H"),
            correctAnswerIndex = 0,
            explanation = "Exp 2"
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { repository.getScenarios() } returns flowOf(mockScenarios)
        viewModel = ActivityViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads scenarios correctly`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(mockScenarios[0], state.currentScenario)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `selecting an answer updates state`() = runTest {
        viewModel.selectAnswer(2)
        assertEquals(2, viewModel.uiState.value.selectedAnswerIndex)
    }

    @Test
    fun `submitting correct answer updates state correctly`() = runTest {
        viewModel.selectAnswer(1) // Correct index for Scenario 1
        viewModel.submitAnswer()
        
        assertTrue(viewModel.uiState.value.isAnswerSubmitted)
        assertTrue(viewModel.uiState.value.isCorrect)
    }

    @Test
    fun `submitting wrong answer updates state correctly`() = runTest {
        viewModel.selectAnswer(0) // Incorrect index
        viewModel.submitAnswer()
        
        assertTrue(viewModel.uiState.value.isAnswerSubmitted)
        assertFalse(viewModel.uiState.value.isCorrect)
    }

    @Test
    fun `navigating to next scenario resets answer state`() = runTest {
        viewModel.selectAnswer(1)
        viewModel.submitAnswer()
        viewModel.nextScenario()
        
        val state = viewModel.uiState.value
        assertEquals(mockScenarios[1], state.currentScenario)
        assertEquals(null, state.selectedAnswerIndex)
        assertFalse(state.isAnswerSubmitted)
    }

    @Test
    fun `finishing all scenarios updates isFinished flag`() = runTest {
        viewModel.nextScenario() // Move to 2nd
        viewModel.nextScenario() // Move to end
        
        assertTrue(viewModel.uiState.value.isFinished)
    }
}
