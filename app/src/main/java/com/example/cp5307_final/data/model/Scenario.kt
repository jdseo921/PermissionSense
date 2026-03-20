package com.example.cp5307_final.data.model

data class Scenario(
    val id: Int,
    val title: String,
    val description: String,
    val options: List<Option>,
    val explanation: String
)

data class Option(
    val id: Int,
    val text: String,
    val isCorrect: Boolean
)
