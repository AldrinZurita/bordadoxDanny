package bo.bordadoxdanny.app.features.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    @Serializable data object Profile : NavRoute()
    @Serializable data object ProfileEdit : NavRoute()
    @Serializable data object Github : NavRoute()
    @Serializable data object Movies : NavRoute()
    @Serializable data object Crypto : NavRoute()
    @Serializable data object FakeStore : NavRoute()
    @Serializable data object CountryStore : NavRoute()
    @Serializable data object Dollar : NavRoute()
    @Serializable data object Testing : NavRoute()
    @Serializable data object Orders : NavRoute()
    @Serializable data object Cash : NavRoute()
    @Serializable data object Reports : NavRoute()
}
