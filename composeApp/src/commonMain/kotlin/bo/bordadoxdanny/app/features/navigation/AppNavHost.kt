package bo.bordadoxdanny.app.features.navigation

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bo.bordadoxdanny.app.core.daemon.ui.DaemonStatusScreen
import bo.bordadoxdanny.app.core.daemon.watchdog.WatchdogViewModel
import bo.bordadoxdanny.app.features.profile.presentation.ProfileScreen
import bo.bordadoxdanny.app.features.testing.presentation.TestingScreen
import bo.bordadoxdanny.app.features.orders.presentation.OrdersScreen
import bo.bordadoxdanny.app.features.orders.presentation.create.CreateOrderScreen
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Testing,
        modifier = modifier
    ) {
        composable<NavRoute.Testing> {
            TestingScreen(
                onNavigateToDaemon = {
                    navController.navigate(NavRoute.Daemon)
                }
            )
        }

        composable<NavRoute.Profile> {
            ProfileScreen()
        }

        composable<NavRoute.Daemon> {
            val viewModel: WatchdogViewModel = koinViewModel()
            DaemonStatusScreen(viewModel = viewModel)
        }

        composable<NavRoute.Orders> {
            OrdersScreen(
                onAddOrder = {
                    navController.navigate(NavRoute.CreateOrder)
                }
            )
        }

        composable<NavRoute.CreateOrder> {
            CreateOrderScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable<NavRoute.Cash> {
            BasicText(
                text = "Pantalla de Caja",
                style = AppTheme.typography.headlineLarge.copy(color = AppTheme.colors.textPrimary)
            )
        }

        composable<NavRoute.Reports> {
            BasicText(
                text = "Pantalla de Reportes",
                style = AppTheme.typography.headlineLarge.copy(color = AppTheme.colors.textPrimary)
            )
        }
    }
}
