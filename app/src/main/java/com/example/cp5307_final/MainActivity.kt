package com.example.cp5307_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cp5307_final.ui.activity.ActivityScreen
import com.example.cp5307_final.ui.landing.LandingScreen
import com.example.cp5307_final.ui.navigation.NavRoute
import com.example.cp5307_final.ui.navigation.bottomNavItems
import com.example.cp5307_final.ui.settings.LanguageManager
import com.example.cp5307_final.ui.settings.SettingsScreen
import com.example.cp5307_final.ui.settings.SettingsViewModel
import com.example.cp5307_final.ui.statistics.StatisticsScreen
import com.example.cp5307_final.ui.theme.PermissionSenseTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * This is the main starting point of the application.
 * It's like the 'lobby' of a building where everything else is organized.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // We get the user's settings (like dark mode and language)
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val userSettings by settingsViewModel.settings.collectAsState()
            
            // Determine if we should use Dark Mode based on settings or system preference
            val isDarkMode = userSettings?.isDarkMode ?: isSystemInDarkTheme()
            val language = userSettings?.language ?: "English"

            // Set the visual theme for the entire app
            PermissionSenseTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Launch the main 'shell' of the app which handles navigation
                    PermissionSenseAppShell(language)
                }
            }
        }
    }
}

/**
 * This function sets up the main layout of the app, including the bottom navigation bar.
 */
@Composable
fun PermissionSenseAppShell(language: String) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Translation helper to get words in the correct language
    val t = { key: String -> LanguageManager.getTranslation(key, language) }

    // This function handles switching between the different tabs (Home, Activity, etc.)
    val navigateToTab = { route: NavRoute ->
        navController.navigate(route) {
            // Clears the history so we don't build up a huge stack of screens
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoids making a new copy of the screen if it's already on top
            launchSingleTop = true
            // Remembers what was happening on that screen before we left it
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            // The bottom navigation menu
            NavigationBar {
                bottomNavItems.forEach { item ->
                    // Check if the current tab is the one being drawn
                    val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                    
                    // Translate the labels for the tabs
                    val label = when (item.route) {
                        NavRoute.Landing -> t("home")
                        NavRoute.Activity -> t("mission") // Shortened to 'MISSION' to fit better
                        NavRoute.Statistics -> t("stats")
                        NavRoute.Settings -> t("settings")
                        else -> item.label
                    }
                    
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = label) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        selected = isSelected,
                        onClick = { navigateToTab(item.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // This 'Host' manages which screen is actually shown inside the Scaffold
        NavHost(
            navController = navController,
            startDestination = NavRoute.Landing,
            modifier = Modifier.padding(innerPadding),
            // Smooth animations for when we switch screens
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400)) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400)) + fadeOut()
            }
        ) {
            // Define all the different 'Rooms' (Screens) in our app
            composable<NavRoute.Landing> {
                LandingScreen(
                    onStartActivity = { navigateToTab(NavRoute.Activity) },
                    onViewStatistics = { navigateToTab(NavRoute.Statistics) },
                    onOpenSettings = { navigateToTab(NavRoute.Settings) }
                )
            }
            composable<NavRoute.Activity> {
                ActivityScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable<NavRoute.Settings> {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable<NavRoute.Statistics> {
                StatisticsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
