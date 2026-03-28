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
import com.example.cp5307_final.ui.settings.SettingsScreen
import com.example.cp5307_final.ui.settings.SettingsViewModel
import com.example.cp5307_final.ui.statistics.StatisticsScreen
import com.example.cp5307_final.ui.theme.PermissionSenseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val userSettings by settingsViewModel.settings.collectAsState()
            
            val isDarkMode = userSettings?.isDarkMode ?: isSystemInDarkTheme()

            PermissionSenseTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PermissionSenseAppShell()
                }
            }
        }
    }
}

@Composable
fun PermissionSenseAppShell() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Helper function for navigating to top-level destinations (tabs)
    val navigateToTab = { route: NavRoute ->
        navController.navigate(route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = isSelected,
                        onClick = { navigateToTab(item.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Landing,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
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
