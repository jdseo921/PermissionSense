package com.example.cp5307_final.domain.model

data class Statistics(
    val totalScenarios: Int,
    val completedScenarios: Int,
    val correctAnswers: Int,
    val averageScore: Float,
    val categoryBreakdown: Map<String, CategoryStats>
)

data class CategoryStats(
    val total: Int,
    val completed: Int,
    val correct: Int
)
