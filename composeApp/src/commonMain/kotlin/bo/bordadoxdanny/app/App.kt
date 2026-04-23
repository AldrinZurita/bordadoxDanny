package bo.bordadoxdanny.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.theme.DsTheme
import bo.bordadoxdanny.app.core.designsystem.theme.ThemeMode
import bo.bordadoxdanny.app.features.navigation.Screen
import bo.bordadoxdanny.app.features.navigation.bottomNavItems
import bo.bordadoxdanny.app.features.orders.presentation.OrdersScreen
import bo.bordadoxdanny.app.features.cash.presentation.CashScreen
import bo.bordadoxdanny.app.features.reports.presentation.ReportsScreen
import bo.bordadoxdanny.app.features.profile.presentation.ProfileScreen
import bo.bordadoxdanny.app.features.config.presentation.ConfigScreen

@Composable
fun App() {
    val navController = rememberNavController()
    
    DsTheme(mode = ThemeMode.LIGHT) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = AppTheme.colors.background,
                bottomBar = {
                    NavigationBar(
                        containerColor = AppTheme.colors.surface
                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        
                        bottomNavItems.forEach { screen ->
                            NavigationBarItem(
                                icon = { Icon(Icons.Filled.Home, contentDescription = null, tint = AppTheme.colors.primary) }, 
                                label = { Text(screen.title, color = AppTheme.colors.textPrimary) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().route!!) {
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
                    startDestination = Screen.Config.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.Orders.route) { OrdersScreen() }
                    composable(Screen.Cash.route) { CashScreen() }
                    composable(Screen.Reports.route) { ReportsScreen() }
                    composable(Screen.Profile.route) { ProfileScreen() }
                    composable(Screen.Config.route) { ConfigScreen() }
                }
            }
        }
    }
}
