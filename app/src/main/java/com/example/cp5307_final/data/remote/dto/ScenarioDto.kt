package com.example.cp5307_final.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScenarioDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("scenarioText") val scenarioText: String,
    @SerializedName("category") val category: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("choices") val choices: List<String>,
    @SerializedName("correctAnswerIndex") val correctAnswerIndex: Int,
    @SerializedName("explanation") val explanation: String
)
