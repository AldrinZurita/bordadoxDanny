package bo.bordadoxdanny.app.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import bo.bordadoxdanny.app.core.daemon.ui.DaemonStatusScreen
import bo.bordadoxdanny.app.core.daemon.watchdog.WatchdogViewModel
import bo.bordadoxdanny.app.features.profile.presentation.*
import bo.bordadoxdanny.app.features.testing.presentation.TestingScreen
import bo.bordadoxdanny.app.features.orders.presentation.OrdersScreen
import bo.bordadoxdanny.app.features.orders.presentation.create.CreateOrderScreen
import bo.bordadoxdanny.app.features.reports.presentation.AccountSummaryScreen
import bo.bordadoxdanny.app.features.reports.presentation.ReportViewModel
import bo.bordadoxdanny.app.features.cash.presentation.*
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Orders,
        modifier = modifier
    ) {
        // --- Auth Flows ---
        composable<NavRoute.Login> {
            val viewModel: AuthViewModel = koinViewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigateToMain = {
                    navController.navigate(NavRoute.Orders) {
                        popUpTo(NavRoute.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(NavRoute.Register)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(NavRoute.ForgotPassword)
                }
            )
        }

        composable<NavRoute.Register> {
            val viewModel: AuthViewModel = koinViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToMain = {
                    navController.navigate(NavRoute.Orders) {
                        popUpTo(NavRoute.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<NavRoute.ForgotPassword> {
            val viewModel: AuthViewModel = koinViewModel()
            ForgotPasswordScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToVerification = { email ->
                    navController.navigate(NavRoute.VerificationCode(email))
                }
            )
        }

        composable<NavRoute.VerificationCode> { backStackEntry ->
            val route: NavRoute.VerificationCode = backStackEntry.toRoute()
            val viewModel: AuthViewModel = koinViewModel()
            VerificationCodeScreen(
                email = route.email,
                viewModel = viewModel,
                onNavigateToNewPassword = { email ->
                    navController.navigate(NavRoute.NewPassword(email))
                }
            )
        }

        composable<NavRoute.NewPassword> { backStackEntry ->
            val route: NavRoute.NewPassword = backStackEntry.toRoute()
            val viewModel: AuthViewModel = koinViewModel()
            NewPasswordScreen(
                email = route.email,
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigate(NavRoute.Login) {
                        popUpTo(NavRoute.Login) { inclusive = true }
                    }
                }
            )
        }

        // --- Main App ---
        composable<NavRoute.Testing> {
            TestingScreen(
                onNavigateToDaemon = {
                    navController.navigate(NavRoute.Daemon)
                }
            )
        }

        composable<NavRoute.Profile> {
            val viewModel: ProfileViewModel = koinViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigate(NavRoute.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
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
            CashScreen()
        }

        composable<NavRoute.Reports> {
            val viewModel: ReportViewModel = koinViewModel()
            AccountSummaryScreen(viewModel = viewModel)
        }
    }
}
