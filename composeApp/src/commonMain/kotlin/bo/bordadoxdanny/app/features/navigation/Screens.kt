package bo.bordadoxdanny.app.features.navigation

sealed class Screen(
    val route: NavRoute,
    val title: String
) {
    data object Orders : Screen(NavRoute.Orders, "Órdenes")
    data object Cash : Screen(NavRoute.Cash, "Caja")
    data object Reports : Screen(NavRoute.Reports, "Reportes")
    data object Profile : Screen(NavRoute.Profile, "Perfil")
    data object Testing : Screen(NavRoute.Testing, "Pruebas")

    companion object {
        val bottomNavItems = listOf(Orders, Cash, Reports, Profile, Testing)
    }
}
