package bo.bordadoxdanny.app.features.navigation

import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.nav_orders
import bo.bordadoxdanny.app.nav_cash
import bo.bordadoxdanny.app.nav_reports
import bo.bordadoxdanny.app.nav_profile
import bo.bordadoxdanny.app.nav_testing
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcons
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class Screen(
    val route: NavRoute,
    val titleRes: StringResource,
    val icon: DrawableResource
) {
    data object Orders : Screen(NavRoute.Orders, Res.string.nav_orders, AppIcons.Orders)
    data object Cash : Screen(NavRoute.Cash, Res.string.nav_cash, AppIcons.Cash)
    data object Reports : Screen(NavRoute.Reports, Res.string.nav_reports, AppIcons.Reports)
    data object Profile : Screen(NavRoute.Profile, Res.string.nav_profile, AppIcons.Profile)
    data object Testing : Screen(NavRoute.Testing, Res.string.nav_testing, AppIcons.Testing)

    companion object {
        val bottomNavItems = listOf(Orders, Cash, Reports, Profile, Testing)
    }
}
