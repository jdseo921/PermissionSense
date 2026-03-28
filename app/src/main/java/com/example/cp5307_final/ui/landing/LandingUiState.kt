package com.example.cp5307_final.ui.landing

import com.example.cp5307_final.domain.model.Scenario

data class LandingUiState(
    val isLoading: Boolean = false,
    val todaysChallenge: Scenario? = null,
    val missedScenarios: List<Scenario> = emptyList(),
    val totalCompleted: Int = 0,
    val currentStreak: Int = 0,
    val accuracy: Int = 0,
    val errorMessage: String? = null
)
