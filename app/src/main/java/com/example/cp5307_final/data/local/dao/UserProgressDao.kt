package com.example.cp5307_final.data.local.dao

import androidx.room.*
import com.example.cp5307_final.data.local.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<UserProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: UserProgressEntity)

    @Query("SELECT COUNT(*) FROM user_progress WHERE completed = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM user_progress WHERE isCorrect = 1")
    fun getCorrectCount(): Flow<Int>

    @Query("SELECT * FROM user_progress WHERE scenarioId = :id")
    suspend fun getProgressForScenario(id: Int): UserProgressEntity?

    @Query("DELETE FROM user_progress")
    suspend fun deleteAllProgress()
}
