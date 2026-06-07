package bo.bordadoxdanny.app.features.profile.presentation

import bo.bordadoxdanny.app.features.profile.domain.RegisterParams

sealed class AuthIntent {
    data class OnLogin(val emailOrUser: String, val password: String) : AuthIntent()
    data class OnRegister(val params: RegisterParams) : AuthIntent()
    data class OnSendCode(val emailOrUser: String) : AuthIntent()
    data class OnVerifyCode(val email: String, val code: String) : AuthIntent()
    data class OnResendCode(val email: String) : AuthIntent()
    data class OnResetPassword(val email: String, val newPassword: String) : AuthIntent()
    data class OnPasswordChanged(val value: String) : AuthIntent()
    data class OnConfirmPasswordChanged(val value: String) : AuthIntent()
    data object OnNavigateToRegister : AuthIntent()
    data object OnNavigateToForgotPassword : AuthIntent()
    data object OnNavigateToLogin : AuthIntent()
}
