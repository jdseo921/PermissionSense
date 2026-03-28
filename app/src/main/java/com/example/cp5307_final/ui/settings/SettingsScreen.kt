package com.example.cp5307_final.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    val lang = settings?.language ?: "English"
    val t = { key: String -> LanguageManager.getTranslation(key, lang) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.setDailyReminder(true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(t("settings")) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = t("exit"))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            settings?.let { userSettings ->
                SettingsToggleItem(
                    title = t("dark_mode"),
                    description = t("dark_mode_desc"),
                    checked = userSettings.isDarkMode,
                    onCheckedChange = { viewModel.setDarkMode(it) }
                )
                
                HorizontalDivider()

                SettingsToggleItem(
                    title = t("high_contrast"),
                    description = t("high_contrast_desc"),
                    checked = userSettings.highContrast,
                    onCheckedChange = { viewModel.setHighContrast(it) }
                )

                HorizontalDivider()

                // Language Setting
                Text(text = t("language"), style = MaterialTheme.typography.titleMedium)
                val langOptions = listOf("English", "Mandarin Chinese", "Korean")
                Column(Modifier.selectableGroup()) {
                    langOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (text == userSettings.language),
                                    onClick = { viewModel.setLanguage(text) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == userSettings.language),
                                onClick = null
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }

                HorizontalDivider()

                // Font Size Setting
                Text(text = t("font_size"), style = MaterialTheme.typography.titleMedium)
                val radioOptions = listOf("Small", "Medium", "Large")
                Column(Modifier.selectableGroup()) {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (text == userSettings.textSize),
                                    onClick = { viewModel.setTextSize(text) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == userSettings.textSize),
                                onClick = null
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }

                HorizontalDivider()

                SettingsToggleItem(
                    title = t("daily_reminders"),
                    description = t("daily_reminders_desc"),
                    checked = userSettings.dailyReminder,
                    onCheckedChange = { checked ->
                        if (checked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                viewModel.setDailyReminder(true)
                            }
                        } else {
                            viewModel.setDailyReminder(checked)
                        }
                    }
                )

                if (userSettings.dailyReminder) {
                    TextButton(
                        onClick = { showTimePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${t("reminder_time")}: ${userSettings.reminderTime}")
                    }
                }

                if (showTimePicker) {
                    val timeState = rememberTimePickerState(
                        initialHour = userSettings.reminderTime.split(":")[0].toIntOrNull() ?: 9,
                        initialMinute = userSettings.reminderTime.split(":")[1].toIntOrNull() ?: 0
                    )

                    AlertDialog(
                        onDismissRequest = { showTimePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                val time = String.format(Locale.getDefault(), "%02d:%02d", timeState.hour, timeState.minute)
                                viewModel.setReminderTime(time)
                                showTimePicker = false
                            }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showTimePicker = false }) {
                                Text("Cancel")
                            }
                        },
                        text = {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                TimePicker(state = timeState)
                            }
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.resetProgress() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(t("reset_progress"))
                }
                
                Spacer(Modifier.height(16.dp))
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
