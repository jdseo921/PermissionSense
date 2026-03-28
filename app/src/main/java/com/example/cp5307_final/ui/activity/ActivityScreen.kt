package com.example.cp5307_final.ui.activity

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cp5307_final.ui.settings.LanguageManager
import com.example.cp5307_final.ui.settings.SettingsViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * The Activity Screen is where the user actually takes the privacy challenges.
 * Think of it as the "Game Level" where you answer questions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    // These 'collectAsState' lines let the screen react whenever data changes
    val uiState by viewModel.uiState.collectAsState()
    val settings by settingsViewModel.settings.collectAsState()
    var showConfetti by remember { mutableStateOf(false) }
    
    // Setup translation based on user's chosen language
    val lang = settings?.language ?: "English"
    val t = { key: String -> LanguageManager.getTranslation(key, lang) }

    // This block triggers the confetti effect when the user gets an answer right
    LaunchedEffect(uiState.isCorrect, uiState.isAnswerSubmitted) {
        if (uiState.isCorrect && uiState.isAnswerSubmitted) {
            showConfetti = true
            delay(3000) // Keep the confetti for 3 seconds
            showConfetti = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            topBar = {
                // The title bar at the very top of the screen
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            t("mission_challenge"), 
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        ) 
                    },
                    navigationIcon = {
                        // The 'X' button to quit the mission
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.Close, contentDescription = t("exit"))
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            if (uiState.isLoading) {
                // Show a spinning circle if the data is still loading
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 6.dp)
                }
            } else if (uiState.isFinished) {
                // Show the 'Mission Accomplished' screen when done
                CompletionView(onNavigateBack, t)
            } else {
                uiState.currentScenario?.let { scenario ->
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Progress Bar showing how far along you are in the mission
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    t("mission_progress"),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "${(uiState.progress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            LinearProgressIndicator(
                                progress = { uiState.progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .clip(CircleShape),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        }

                        // The Scenario Card containing the question text
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(28.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Surface(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = scenario.category.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = scenario.title,
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = scenario.scenarioText,
                                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Label for the answer choices
                        Text(
                            t("your_decision"),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = MaterialTheme.colorScheme.outline
                        )

                        // List of possible answers
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                        }

                        // Feedback area: Shows up after you submit an answer
                        AnimatedVisibility(
                            visible = uiState.isAnswerSubmitted,
                            enter = slideInVertically { it } + fadeIn(),
                            exit = slideOutVertically { it } + fadeOut()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                val resultColor = if (uiState.isCorrect) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
                                val resultIcon = if (uiState.isCorrect) Icons.Default.CheckCircle else Icons.Default.Warning

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = resultColor.copy(alpha = 0.1f)
                                    ),
                                    border = BorderStroke(1.dp, resultColor.copy(alpha = 0.2f))
                                ) {
                                    Column(modifier = Modifier.padding(24.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(resultIcon, contentDescription = null, tint = resultColor)
                                            Spacer(Modifier.width(12.dp))
                                            Text(
                                                text = if (uiState.isCorrect) t("secure_choice") else t("privacy_risk"),
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                color = resultColor
                                            )
                                        }
                                        Spacer(Modifier.height(16.dp))
                                        // Explains WHY the answer was right or wrong
                                        Text(
                                            text = scenario.explanation,
                                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                // Button to go to the next question
                                Button(
                                    onClick = { viewModel.nextScenario() },
                                    modifier = Modifier.fillMaxWidth().height(60.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(t("proceed_next"), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                                    Spacer(Modifier.width(8.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                                }
                            }
                        }

                        // Confirm button: Only shown before you submit your choice
                        if (!uiState.isAnswerSubmitted) {
                            Button(
                                onClick = { viewModel.submitAnswer() },
                                modifier = Modifier.fillMaxWidth().height(60.dp),
                                enabled = uiState.selectedAnswerIndex != null,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(t("confirm_decision"), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                        
                        Spacer(Modifier.height(40.dp))
                    }
                }
            }
        }

        // The celebratory effect for correct answers
        if (showConfetti) {
            ConfettiEffect()
        }
    }
}

/**
 * A custom card for each multiple-choice answer.
 * It changes color based on whether it's selected and if the result is shown.
 */
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
        showResult && isCorrect -> Color(0xFF4CAF50) // Green for correct
        showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error // Red for wrong selection
        isSelected -> MaterialTheme.colorScheme.primary // Blue for current selection
        else -> MaterialTheme.colorScheme.outlineVariant // Gray for others
    }

    val containerColor = when {
        showResult && isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.08f)
        showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.surface
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(if (isSelected || (showResult && isCorrect)) 2.dp else 1.dp, borderColor),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // A small circular radio button or check/cross icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) borderColor else Color.Transparent)
                    .border(2.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected || (showResult && isCorrect)) {
                    Icon(
                        if (showResult && !isCorrect && isSelected) Icons.Default.Close else Icons.Default.Check,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else borderColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * The screen shown after all questions in a mission are answered.
 */
@Composable
fun CompletionView(onFinish: () -> Unit, t: (String) -> String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.size(160.dp),
                color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                shape = CircleShape
            ) {}
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = Color(0xFF4CAF50)
            )
        }
        
        Spacer(Modifier.height(32.dp))
        Text(
            t("mission_accomplished"), 
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            t("completion_desc"),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(Modifier.height(48.dp))
        
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(t("return_center"), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}

/**
 * Creates a fun animation of falling colorful particles.
 */
@Composable
fun ConfettiEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    
    // Create a list of particles with random positions and colors
    val particles = remember {
        List(40) {
            ConfettiParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                color = listOf(Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFFC107), Color(0xFFE91E63), Color(0xFF9C27B0)).random(),
                size = Random.nextFloat() * 12f + 8f,
                speed = Random.nextFloat() * 2500f + 1500f
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            // Animate each particle from top to bottom
            val animY = infiniteTransition.animateFloat(
                initialValue = -0.1f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(particle.speed.toInt(), easing = LinearOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "y"
            )

            val rotation = infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing)
                ),
                label = "rotation"
            )

            Box(
                modifier = Modifier
                    .offset(
                        x = (particle.x * 400).dp,
                        y = (animY.value * 800).dp
                    )
                    .size(particle.size.dp)
                    .graphicsLayer(rotationZ = rotation.value)
                    .background(particle.color, shape = if (Random.nextBoolean()) CircleShape else RoundedCornerShape(2.dp))
            )
        }
    }
}

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val color: Color,
    val size: Float,
    val speed: Float
)
