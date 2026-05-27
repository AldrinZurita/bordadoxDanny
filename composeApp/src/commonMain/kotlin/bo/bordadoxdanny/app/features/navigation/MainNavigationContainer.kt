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
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon

@Composable
fun MainNavigationContainer(
    onResetOnboarding: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = Screen.bottomNavItems.any { screen ->
        currentDestination?.hasRoute(screen.route::class) == true
    } || currentDestination?.hasRoute(NavRoute.Testing::class) == true

    Column(modifier = Modifier.fillMaxSize().background(AppTheme.colors.background)) {
        Box(modifier = Modifier.weight(1f)) {
            AppNavHost(
                navController = navController,
                onResetOnboarding = onResetOnboarding
            )
        }

        if (showBottomBar) {
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
                                    popUpTo(NavRoute.Testing) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AppIcon(
                                resource = screen.icon,
                                contentDescription = screen.title,
                                tint = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            BasicText(
                                text = screen.title,
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
}
