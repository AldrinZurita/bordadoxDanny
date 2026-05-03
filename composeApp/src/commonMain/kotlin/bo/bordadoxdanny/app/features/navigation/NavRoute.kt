package bo.bordadoxdanny.app.features.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    @Serializable data object Profile : NavRoute()

    @Serializable data object Orders : NavRoute()
    @Serializable data object Cash : NavRoute()
    @Serializable data object Reports : NavRoute()

    @Serializable data object Testing : NavRoute()
    @Serializable data object Daemon : NavRoute()
}
