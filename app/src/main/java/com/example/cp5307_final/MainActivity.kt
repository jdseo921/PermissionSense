package com.example.cp5307_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.example.cp5307_final.ui.statistics.StatisticsScreen
import com.example.cp5307_final.ui.theme.PermissionSenseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionSenseTheme {
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

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Landing,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<NavRoute.Landing> {
                LandingScreen(
                    onStartActivity = { navController.navigate(NavRoute.Activity) },
                    onViewStatistics = { navController.navigate(NavRoute.Statistics) },
                    onOpenSettings = { navController.navigate(NavRoute.Settings) }
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
