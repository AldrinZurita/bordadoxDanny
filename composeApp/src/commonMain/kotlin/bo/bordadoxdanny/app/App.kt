package bo.bordadoxdanny.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bo.bordadoxdanny.app.features.navigation.Screen
import bo.bordadoxdanny.app.features.navigation.bottomNavItems
import bo.bordadoxdanny.app.features.orders.presentation.OrdersScreen
import bo.bordadoxdanny.app.features.cash.presentation.CashScreen
import bo.bordadoxdanny.app.features.reports.presentation.ReportsScreen
import bo.bordadoxdanny.app.features.profile.presentation.ProfileScreen
import org.koin.compose.KoinContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    KoinContext {
        MaterialTheme {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        bottomNavItems.forEach { screen ->
                            NavigationBarItem(
                                icon = { 
                                    Icon(
                                        imageVector = getScreenIcon(screen), 
                                        contentDescription = screen.title
                                    ) 
                                },
                                label = { Text(screen.title) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().route ?: Screen.Orders.route) {
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
                    startDestination = Screen.Orders.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.Orders.route) {
                        OrdersScreen()
                    }
                    composable(Screen.Cash.route) {
                        CashScreen()
                    }
                    composable(Screen.Reports.route) {
                        ReportsScreen()
                    }
                    composable(Screen.Profile.route) {
                        ProfileScreen()
                    }
                }
            }
        }
    }
}

private fun getScreenIcon(screen: Screen): ImageVector {
    return when (screen) {
        Screen.Orders -> Icons.Default.ShoppingCart
        Screen.Cash -> Icons.AutoMirrored.Filled.List
        Screen.Reports -> Icons.Default.Notifications
        Screen.Profile -> Icons.Default.AccountCircle
    }
}
