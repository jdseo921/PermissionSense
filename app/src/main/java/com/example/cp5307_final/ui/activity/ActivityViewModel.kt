package com.example.cp5307_final.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cp5307_final.domain.model.Scenario
import com.example.cp5307_final.domain.model.UserProgress
import com.example.cp5307_final.domain.repository.ScenarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repository: ScenarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    private var allScenarios: List<Scenario> = emptyList()
    private var currentIndex = 0

    init {
        loadScenarios()
    }

    private fun loadScenarios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getScenarios().collect { scenarios ->
                allScenarios = scenarios
                if (scenarios.isNotEmpty()) {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            currentScenario = scenarios[currentIndex]
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "No scenarios found") }
                }
            }
        }
    }

    fun selectAnswer(index: Int) {
        if (_uiState.value.isAnswerSubmitted) return
        _uiState.update { it.copy(selectedAnswerIndex = index) }
    }

    fun submitAnswer() {
        val state = _uiState.value
        val scenario = state.currentScenario ?: return
        val selected = state.selectedAnswerIndex ?: return

        val correct = selected == scenario.correctAnswerIndex

        viewModelScope.launch {
            repository.saveProgress(
                UserProgress(
                    scenarioId = scenario.id,
                    completed = true,
                    selectedAnswerIndex = selected,
                    isCorrect = correct,
                    lastAttemptedAt = Date().time
                )
            )
        }

        _uiState.update {
            it.copy(
                isAnswerSubmitted = true,
                isCorrect = correct
            )
        }
    }

    fun nextScenario() {
        currentIndex++
        if (currentIndex < allScenarios.size) {
            _uiState.update {
                it.copy(
                    currentScenario = allScenarios[currentIndex],
                    selectedAnswerIndex = null,
                    isAnswerSubmitted = false,
                    isCorrect = false
                )
            }
        } else {
            _uiState.update { it.copy(isFinished = true) }
        }
    }
}
