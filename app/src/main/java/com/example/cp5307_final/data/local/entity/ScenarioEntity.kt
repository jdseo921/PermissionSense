package com.example.cp5307_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scenarios")
data class ScenarioEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val scenarioText: String,
    val category: String,
    val difficulty: String,
    val choice1: String,
    val choice2: String,
    val choice3: String,
    val choice4: String,
    val correctAnswerIndex: Int,
    val explanation: String
)
