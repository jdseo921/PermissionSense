package com.example.cp5307_final.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val DAILY_REMINDER = booleanPreferencesKey("daily_reminder")
        val REMINDER_TIME = stringPreferencesKey("reminder_time")
        val TEXT_SIZE = stringPreferencesKey("text_size")
        val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
        val LANGUAGE = stringPreferencesKey("language")
    }

    val settings: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            isDarkMode = preferences[Keys.IS_DARK_MODE] ?: false,
            dailyReminder = preferences[Keys.DAILY_REMINDER] ?: false,
            reminderTime = preferences[Keys.REMINDER_TIME] ?: "09:00",
            textSize = preferences[Keys.TEXT_SIZE] ?: "Medium",
            highContrast = preferences[Keys.HIGH_CONTRAST] ?: false,
            language = preferences[Keys.LANGUAGE] ?: "English"
        )
    }

    suspend fun updateDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[Keys.IS_DARK_MODE] = enabled }
    }

    suspend fun updateDailyReminder(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DAILY_REMINDER] = enabled }
    }

    suspend fun updateReminderTime(time: String) {
        context.dataStore.edit { it[Keys.REMINDER_TIME] = time }
    }

    suspend fun updateTextSize(size: String) {
        context.dataStore.edit { it[Keys.TEXT_SIZE] = size }
    }

    suspend fun updateHighContrast(enabled: Boolean) {
        context.dataStore.edit { it[Keys.HIGH_CONTRAST] = enabled }
    }

    suspend fun updateLanguage(language: String) {
        context.dataStore.edit { it[Keys.LANGUAGE] = language }
    }
}

data class UserSettings(
    val isDarkMode: Boolean,
    val dailyReminder: Boolean,
    val reminderTime: String,
    val textSize: String,
    val highContrast: Boolean,
    val language: String
)
