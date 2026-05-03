package bo.bordadoxdanny.app.features.navigation

import bo.bordadoxdanny.app.core.designsystem.theme.AppIcons
import org.jetbrains.compose.resources.DrawableResource

sealed class Screen(
    val route: NavRoute,
    val title: String,
    val icon: DrawableResource
) {
    data object Orders : Screen(NavRoute.Orders, "Órdenes", AppIcons.Account)
    data object Cash : Screen(NavRoute.Cash, "Caja", AppIcons.Account)
    data object Reports : Screen(NavRoute.Reports, "Reportes", AppIcons.Account)
    data object Profile : Screen(NavRoute.Profile, "Perfil", AppIcons.Account)
    data object Testing : Screen(NavRoute.Testing, "Testing", AppIcons.Account)

    companion object {
        val bottomNavItems = listOf(Orders, Cash, Reports, Profile, Testing)
    }
}
