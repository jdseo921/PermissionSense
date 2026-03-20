package com.example.cp5307_final.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cp5307_final.data.local.PreferencesManager
import com.example.cp5307_final.data.local.UserSettings
import com.example.cp5307_final.domain.repository.ScenarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val repository: ScenarioRepository
) : ViewModel() {

    val settings: StateFlow<UserSettings?> = preferencesManager.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.updateDarkMode(enabled) }
    }

    fun setDailyReminder(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.updateDailyReminder(enabled) }
    }

    fun setReminderTime(time: String) {
        viewModelScope.launch { preferencesManager.updateReminderTime(time) }
    }

    fun setTextSize(size: String) {
        viewModelScope.launch { preferencesManager.updateTextSize(size) }
    }

    fun setHighContrast(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.updateHighContrast(enabled) }
    }

    fun resetProgress() {
        viewModelScope.launch {
            // repository.resetUserProgress() 
        }
    }
}
