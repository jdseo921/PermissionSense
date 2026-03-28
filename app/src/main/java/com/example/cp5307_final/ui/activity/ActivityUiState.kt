package com.example.cp5307_final.ui.activity

import com.example.cp5307_final.domain.model.Scenario

data class ActivityUiState(
    val isLoading: Boolean = false,
    val currentScenario: Scenario? = null,
    val selectedAnswerIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val errorMessage: String? = null,
    val isFinished: Boolean = false,
    val currentStep: Int = 0,
    val totalSteps: Int = 0
) {
    val progress: Float
        get() = if (totalSteps > 0) (currentStep.toFloat() / totalSteps.toFloat()) else 0f
}
