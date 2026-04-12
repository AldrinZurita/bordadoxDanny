package bo.bordadoxdanny.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bo.bordadoxdanny.app.presentation.navigation.Screen
import bo.bordadoxdanny.app.presentation.navigation.bottomNavItems
import bo.bordadoxdanny.app.presentation.orders.OrdersViewModel
import bo.bordadoxdanny.app.presentation.cash.CashViewModel
import bo.bordadoxdanny.app.presentation.reports.ReportsViewModel
import bo.bordadoxdanny.app.presentation.profile.ProfileViewModel
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

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

@Composable
fun OrdersScreen(viewModel: OrdersViewModel = koinViewModel()) {
    val orders by viewModel.orders.collectAsState()
    Column {
        Text(
            text = "Órdenes", 
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(orders) { order ->
                Divider()
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = order.description, style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Bs. ${order.amount}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
fun CashScreen(viewModel: CashViewModel = koinViewModel()) {
    val entries by viewModel.cashEntries.collectAsState()
    Column {
        Text(
            text = "Caja", 
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(entries) { entry ->
                Divider()
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = entry.reason, style = MaterialTheme.typography.bodyLarge)
                    Text(text = "${entry.type}: Bs. ${entry.amount}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = koinViewModel()) {
    val reports by viewModel.reports.collectAsState()
    Column {
        Text(
            text = "Reportes", 
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(reports) { report ->
                Divider()
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = report.title, style = MaterialTheme.typography.bodyLarge)
                    Text(text = report.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val profile by viewModel.profile.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Perfil", style = MaterialTheme.typography.headlineMedium)
        profile?.let {
            Text("Nombre: ${it.name}", modifier = Modifier.padding(top = 8.dp))
            Text("Email: ${it.email}")
            Text("Teléfono: ${it.phone}")
        } ?: Text("Cargando perfil...")
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
