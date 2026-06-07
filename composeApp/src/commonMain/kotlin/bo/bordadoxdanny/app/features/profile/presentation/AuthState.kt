package bo.bordadoxdanny.app.features.profile.presentation

import org.jetbrains.compose.resources.StringResource

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object LoginSuccess : AuthState()
    data class LoginError(val messageResId: StringResource) : AuthState()
    data object RegisterSuccess : AuthState()
    data class RegisterError(val field: String, val messageResId: StringResource) : AuthState()
    data class CodeSent(val email: String) : AuthState()
    data class CodeSentError(val messageResId: StringResource) : AuthState()
    data class CodeVerified(val email: String) : AuthState()
    data class CodeVerifiedError(val messageResId: StringResource) : AuthState()
    data object PasswordResetSuccess : AuthState()
    data class PasswordResetError(val messageResId: StringResource) : AuthState()
}
