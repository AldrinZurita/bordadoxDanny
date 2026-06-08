package bo.bordadoxdanny.app.features.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    @Serializable data object Profile : NavRoute()
    @Serializable data object Orders : NavRoute()
    @Serializable data object CreateOrder : NavRoute()
    @Serializable data object Cash : NavRoute()
    @Serializable data object Reports : NavRoute()
    
    // Auth Routes
    @Serializable data object Login : NavRoute()
    @Serializable data object Register : NavRoute()
    @Serializable data object ForgotPassword : NavRoute()
    @Serializable data class VerificationCode(val email: String) : NavRoute()
    @Serializable data class NewPassword(val email: String) : NavRoute()

    @Serializable data object Testing : NavRoute()
    @Serializable data object Daemon : NavRoute()
}
