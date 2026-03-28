package com.example.cp5307_final.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cp5307_final.domain.repository.ScenarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val repository: ScenarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState.asStateFlow()

    init {
        loadLandingData()
    }

    private fun loadLandingData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                combine(
                    repository.getScenarios(),
                    repository.getStatistics()
                ) { scenarios, stats ->
                    scenarios to stats
                }.collect { (scenarios, stats) ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            // For now just picking the first one as a challenge
                            todaysChallenge = scenarios.firstOrNull(),
                            totalCompleted = stats.completedScenarios,
                            currentStreak = calculateStreak(), // Still mock or implement
                            accuracy = stats.averageScore.toInt()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    private fun calculateStreak(): Int {
        // Mock streak for now, ideally this would be calculated from progress timestamps
        return 5
    }
}
