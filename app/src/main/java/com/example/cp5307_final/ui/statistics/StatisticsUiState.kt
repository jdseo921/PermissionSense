package com.example.cp5307_final.ui.statistics

import com.example.cp5307_final.domain.model.Statistics

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val statistics: Statistics? = null,
    val errorMessage: String? = null
)
