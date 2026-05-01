package bo.bordadoxdanny.app.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bo.bordadoxdanny.app.core.daemon.ui.DaemonStatusScreen
import bo.bordadoxdanny.app.core.daemon.watchdog.WatchdogViewModel
import bo.bordadoxdanny.app.features.profile.presentation.ProfileScreen
import bo.bordadoxdanny.app.features.testing.presentation.TestingScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, 
        startDestination = NavRoute.Testing
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
            // Using koinViewModel to ensure it's scoped to the backstack entry
            val viewModel: WatchdogViewModel = koinViewModel()
            DaemonStatusScreen(viewModel = viewModel)
        }

        composable<NavRoute.ProfileEdit> {
            // ProfileEditScreen()
        }

        composable<NavRoute.Github> {
            // GithubScreen()
        }

        composable<NavRoute.Crypto> {
            // CryptoScreen()
        }

        composable<NavRoute.FakeStore> {
            // StoreScreen()
        }

        composable<NavRoute.CountryStore> {
            // CountryScreen()
        }

        composable<NavRoute.Dollar> {
            // DollarScreen()
        }

        composable<NavRoute.Orders> {
            // OrdersScreen()
        }

        composable<NavRoute.Cash> {
            // CashScreen()
        }

        composable<NavRoute.Reports> {
            // ReportsScreen()
        }
    }
}
