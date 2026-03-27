package com.example.cp5307_final.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            settings?.let { userSettings ->
                SettingsToggleItem(
                    title = "Dark Mode",
                    description = "Enable dark theme for the app",
                    checked = userSettings.isDarkMode,
                    onCheckedChange = { viewModel.setDarkMode(it) }
                )
                
                HorizontalDivider()

                SettingsToggleItem(
                    title = "High Contrast",
                    description = "Increase accessibility contrast",
                    checked = userSettings.highContrast,
                    onCheckedChange = { viewModel.setHighContrast(it) }
                )

                HorizontalDivider()

                SettingsToggleItem(
                    title = "Daily Reminders",
                    description = "Get notified to complete daily challenges",
                    checked = userSettings.dailyReminder,
                    onCheckedChange = { viewModel.setDailyReminder(it) }
                )

                if (userSettings.dailyReminder) {
                    TextButton(onClick = { /* Open Time Picker */ }) {
                        Text("Reminder Time: ${userSettings.reminderTime}")
                    }
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = { viewModel.resetProgress() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reset All Progress")
                }
            }
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = description, style = MaterialTheme.typography.bodySmall)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
