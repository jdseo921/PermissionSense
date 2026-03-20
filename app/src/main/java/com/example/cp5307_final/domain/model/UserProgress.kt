package com.example.cp5307_final.domain.model

data class UserProgress(
    val scenarioId: Int,
    val completed: Boolean,
    val selectedAnswerIndex: Int?,
    val isCorrect: Boolean,
    val lastAttemptedAt: Long
)
