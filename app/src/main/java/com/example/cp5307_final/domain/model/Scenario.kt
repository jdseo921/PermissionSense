package com.example.cp5307_final.domain.model

data class Scenario(
    val id: Int,
    val title: String,
    val scenarioText: String,
    val category: String,
    val difficulty: String,
    val choices: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)
