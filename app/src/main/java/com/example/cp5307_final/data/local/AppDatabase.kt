package com.example.cp5307_final.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cp5307_final.data.local.dao.ScenarioDao
import com.example.cp5307_final.data.local.dao.UserProgressDao
import com.example.cp5307_final.data.local.entity.ScenarioEntity
import com.example.cp5307_final.data.local.entity.UserProgressEntity

@Database(
    entities = [ScenarioEntity::class, UserProgressEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scenarioDao(): ScenarioDao
    abstract fun userProgressDao(): UserProgressDao
}
