package com.example.cp5307_final.ui.activity

import com.example.cp5307_final.domain.model.Scenario

data class ActivityUiState(
    val isLoading: Boolean = false,
    val currentScenario: Scenario? = null,
    val selectedAnswerIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val errorMessage: String? = null,
    val isFinished: Boolean = false
)
