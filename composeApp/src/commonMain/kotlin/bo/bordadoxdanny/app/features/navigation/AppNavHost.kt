package bo.bordadoxdanny.app.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bo.bordadoxdanny.app.features.profile.presentation.ProfileScreen
import bo.bordadoxdanny.app.features.testing.presentation.TestingScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, 
        startDestination = NavRoute.Testing
    ) {
        composable<NavRoute.Testing> {
            TestingScreen()
        }
        
        composable<NavRoute.Profile> {
            ProfileScreen()
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
