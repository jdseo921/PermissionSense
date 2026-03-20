package com.example.cp5307_final.data.remote

import com.example.cp5307_final.data.remote.dto.ScenarioDto
import retrofit2.http.GET

interface ApiService {
    @GET("scenarios.json")
    suspend fun getScenarios(): List<ScenarioDto>
}
