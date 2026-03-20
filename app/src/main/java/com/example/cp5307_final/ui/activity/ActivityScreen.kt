package com.example.cp5307_final.ui.activity

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Scenario Challenge") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) { Text("Exit") }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.isFinished) {
            CompletionView(onNavigateBack)
        } else {
            uiState.currentScenario?.let { scenario ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Scenario Text Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = scenario.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = scenario.scenarioText,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    // Answer Options
                    Text("Select the safest choice:", style = MaterialTheme.typography.labelLarge)
                    
                    scenario.choices.forEachIndexed { index, choice ->
                        AnswerOptionCard(
                            text = choice,
                            isSelected = uiState.selectedAnswerIndex == index,
                            isCorrect = scenario.correctAnswerIndex == index,
                            showResult = uiState.isAnswerSubmitted,
                            onClick = { viewModel.selectAnswer(index) },
                            enabled = !uiState.isAnswerSubmitted
                        )
                    }

                    // Result Area
                    AnimatedVisibility(
                        visible = uiState.isAnswerSubmitted,
                        enter = expandVertically() + fadeIn()
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            ResultHeader(isCorrect = uiState.isCorrect)
                            
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Explanation",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = scenario.explanation)
                                }
                            }

                            Button(
                                onClick = { viewModel.nextScenario() },
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                Text("Next Challenge")
                            }
                        }
                    }

                    // Submit Button (only show if not submitted)
                    if (!uiState.isAnswerSubmitted) {
                        Button(
                            onClick = { viewModel.submitAnswer() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.selectedAnswerIndex != null,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text("Submit Answer")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnswerOptionCard(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showResult: Boolean,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val borderColor = when {
        showResult && isCorrect -> Color.Green
        showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    val containerColor = when {
        showResult && isCorrect -> Color.Green.copy(alpha = 0.1f)
        showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }

    OutlinedCard(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(if (isSelected || showResult) 2.dp else 1.dp, borderColor),
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            if (showResult) {
                if (isCorrect) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Green)
                } else if (isSelected) {
                    Icon(Icons.Default.Clear, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun ResultHeader(isCorrect: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = if (isCorrect) "Correct!" else "Incorrect",
            style = MaterialTheme.typography.headlineSmall,
            color = if (isCorrect) Color.Green else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CompletionView(onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Activity Complete!", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("You've finished all the current challenges. Great job!")
        Spacer(Modifier.height(32.dp))
        Button(onClick = onFinish) {
            Text("Back to Home")
        }
    }
}
