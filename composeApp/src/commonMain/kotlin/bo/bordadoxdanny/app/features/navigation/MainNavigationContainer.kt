package bo.bordadoxdanny.app.features.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider

@Composable
fun MainNavigationContainer() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Determinamos si debemos mostrar la barra (puedes excluir Login/Registro aquí)
    val showBottomBar = Screen.bottomNavItems.any { screen ->
        currentDestination?.hasRoute(screen.route::class) == true
    } || currentDestination?.hasRoute(NavRoute.Testing::class) == true

    Column(modifier = Modifier.fillMaxSize().background(AppTheme.colors.background)) {
        // Área de contenido dinámico (Ocupa todo el espacio de arriba)
        Box(modifier = Modifier.weight(1f)) {
            AppNavHost(navController = navController)
        }

        // Barra de navegación personalizada (Usando tu designsystem)
        if (showBottomBar) {
            // Usamos el divisor de tu designsystem
            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(AppTheme.colors.surface)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Screen.bottomNavItems.forEach { screen ->
                    val isSelected = currentDestination?.hasRoute(screen.route::class) == true
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                navController.navigate(screen.route) {
                                    // Usamos la ruta inicial Testing para limpiar la pila
                                    popUpTo(NavRoute.Testing) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(
                            text = screen.title,
                            // Color primario si está seleccionado, textPrimary si no
                            style = AppTheme.typography.labelLarge.copy(
                                color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}
