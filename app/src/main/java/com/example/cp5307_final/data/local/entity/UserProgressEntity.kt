package com.example.cp5307_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val scenarioId: Int,
    val completed: Boolean,
    val selectedAnswerIndex: Int?,
    val isCorrect: Boolean,
    val lastAttemptedAt: Long
)
