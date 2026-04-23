package bo.bordadoxdanny.app.features.navigation

sealed class Screen(
    val route: String,
    val title: String
) {
    object Orders : Screen("orders", "Órdenes")
    object Cash : Screen("cash", "Caja")
    object Reports : Screen("reports", "Reportes")
    object Profile : Screen("profile", "Perfil")
    object Config : Screen("config", "Configuración")
}

val bottomNavItems = listOf(
    Screen.Orders,
    Screen.Cash,
    Screen.Reports,
    Screen.Profile,
    Screen.Config
)
