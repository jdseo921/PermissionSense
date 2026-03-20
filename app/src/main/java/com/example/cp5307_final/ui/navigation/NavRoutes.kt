package com.example.cp5307_final.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object Landing : NavRoute
    
    @Serializable
    data object Activity : NavRoute
    
    @Serializable
    data object Settings : NavRoute
    
    @Serializable
    data object Statistics : NavRoute
}

data class BottomNavItem(
    val route: NavRoute,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(NavRoute.Landing, Icons.Default.Home, "Home"),
    BottomNavItem(NavRoute.Activity, Icons.Default.PlayArrow, "Activity"),
    BottomNavItem(NavRoute.Statistics, Icons.Default.Star, "Stats"),
    BottomNavItem(NavRoute.Settings, Icons.Default.Settings, "Settings")
)
