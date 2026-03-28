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

/**
 * The Settings Screen allows the user to customize the app.
 * You can change the language, turn on Dark Mode, or reset your progress here.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    // Get language and translation helper
    val lang = settings?.language ?: "English"
    val t = { key: String -> LanguageManager.getTranslation(key, lang) }

    // This helps ask the Android system for permission to show notifications
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
                // Dark Mode Switch
                SettingsToggleItem(
                    title = t("dark_mode"),
                    description = t("dark_mode_desc"),
                    checked = userSettings.isDarkMode,
                    onCheckedChange = { viewModel.setDarkMode(it) }
                )
                
                HorizontalDivider()

                // Accessibility: High Contrast Switch
                SettingsToggleItem(
                    title = t("high_contrast"),
                    description = t("high_contrast_desc"),
                    checked = userSettings.highContrast,
                    onCheckedChange = { viewModel.setHighContrast(it) }
                )

                HorizontalDivider()

                // Language Selection: Radio buttons for English, Chinese, or Korean
                Text(text = t("language"), style = MaterialTheme.typography.titleMedium)
                val langOptions = listOf(
                    "English" to "lang_english",
                    "Chinese" to "lang_chinese",
                    "Korean" to "lang_korean"
                )
                Column(Modifier.selectableGroup()) {
                    langOptions.forEach { (langName, langKey) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (langName == userSettings.language),
                                    onClick = { viewModel.setLanguage(langName) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (langName == userSettings.language),
                                onClick = null
                            )
                            Text(
                                text = t(langKey),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }

                HorizontalDivider()

                // Font Size Selection
                Text(text = t("font_size"), style = MaterialTheme.typography.titleMedium)
                val radioOptions = listOf(
                    "Small" to "size_small",
                    "Medium" to "size_medium",
                    "Large" to "size_large"
                )
                Column(Modifier.selectableGroup()) {
                    radioOptions.forEach { (sizeName, sizeKey) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (sizeName == userSettings.textSize),
                                    onClick = { viewModel.setTextSize(sizeName) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (sizeName == userSettings.textSize),
                                onClick = null
                            )
                            Text(
                                text = t(sizeKey),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }

                HorizontalDivider()

                // Daily Reminders: Asks for notification permission on modern Android versions
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

                // If reminders are on, show a button to pick the time
                if (userSettings.dailyReminder) {
                    TextButton(
                        onClick = { showTimePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${t("reminder_time")}: ${userSettings.reminderTime}")
                    }
                }

                // A pop-up clock to choose the reminder time
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

                // Reset Button: Deletes all the user's mission progress
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

/**
 * A simple row with a title, description, and an On/Off switch.
 */
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
