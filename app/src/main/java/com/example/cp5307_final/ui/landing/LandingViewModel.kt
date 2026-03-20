package com.example.cp5307_final.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cp5307_final.domain.repository.ScenarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                // In a real app, we would fetch these from local/remote sources
                repository.getScenarios().collect { scenarios ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            todaysChallenge = scenarios.firstOrNull(),
                            totalCompleted = 12, // Mock data
                            currentStreak = 5,   // Mock data
                            accuracy = 85        // Mock data
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}
