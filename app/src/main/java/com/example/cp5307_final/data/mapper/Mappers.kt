package com.example.cp5307_final.data.mapper

import com.example.cp5307_final.data.local.entity.ScenarioEntity
import com.example.cp5307_final.data.local.entity.UserProgressEntity
import com.example.cp5307_final.data.remote.dto.ScenarioDto
import com.example.cp5307_final.domain.model.Scenario
import com.example.cp5307_final.domain.model.UserProgress

fun ScenarioEntity.toDomain(): Scenario {
    return Scenario(
        id = id,
        title = title,
        scenarioText = scenarioText,
        category = category,
        difficulty = difficulty,
        choices = listOf(choice1, choice2, choice3, choice4),
        correctAnswerIndex = correctAnswerIndex,
        explanation = explanation
    )
}

fun Scenario.toEntity(): ScenarioEntity {
    return ScenarioEntity(
        id = id,
        title = title,
        scenarioText = scenarioText,
        category = category,
        difficulty = difficulty,
        choice1 = choices.getOrElse(0) { "" },
        choice2 = choices.getOrElse(1) { "" },
        choice3 = choices.getOrElse(2) { "" },
        choice4 = choices.getOrElse(3) { "" },
        correctAnswerIndex = correctAnswerIndex,
        explanation = explanation
    )
}

fun ScenarioDto.toEntity(): ScenarioEntity {
    return ScenarioEntity(
        id = id,
        title = title,
        scenarioText = scenarioText,
        category = category,
        difficulty = difficulty,
        choice1 = choices.getOrElse(0) { "" },
        choice2 = choices.getOrElse(1) { "" },
        choice3 = choices.getOrElse(2) { "" },
        choice4 = choices.getOrElse(3) { "" },
        correctAnswerIndex = correctAnswerIndex,
        explanation = explanation
    )
}

fun UserProgressEntity.toDomain(): UserProgress {
    return UserProgress(
        scenarioId = scenarioId,
        completed = completed,
        selectedAnswerIndex = selectedAnswerIndex,
        isCorrect = isCorrect,
        lastAttemptedAt = lastAttemptedAt
    )
}

fun UserProgress.toEntity(): UserProgressEntity {
    return UserProgressEntity(
        scenarioId = scenarioId,
        completed = completed,
        selectedAnswerIndex = selectedAnswerIndex,
        isCorrect = isCorrect,
        lastAttemptedAt = lastAttemptedAt
    )
}
