package bo.bordadoxdanny.app.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.profile.domain.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import bo.bordadoxdanny.app.*

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _passwordRequirements = MutableStateFlow(PasswordRequirements())
    val passwordRequirements: StateFlow<PasswordRequirements> = _passwordRequirements.asStateFlow()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.OnLogin -> login(intent.emailOrUser, intent.password)
            is AuthIntent.OnRegister -> register(intent.params, intent.confirmPassword)
            is AuthIntent.OnSendCode -> sendCode(intent.emailOrUser)
            is AuthIntent.OnVerifyCode -> verifyCode(intent.email, intent.code)
            is AuthIntent.OnResetPassword -> resetPassword(intent.email, intent.newPassword)
            is AuthIntent.OnPasswordChanged -> {
                validatePassword(intent.value)
                clearErrorState()
            }
            is AuthIntent.OnConfirmPasswordChanged -> clearErrorState()
            else -> {} // Navigation intents handled by UI
        }
    }

    private fun clearErrorState() {
        if (_state.value is AuthState.PasswordResetError || _state.value is AuthState.CodeVerifiedError) {
            _state.value = AuthState.Idle
        }
    }

    private fun login(emailOrUser: String, password: String) {
        if (_state.value is AuthState.Loading) return
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                loginUseCase(emailOrUser, password).fold(
                    onSuccess = { _state.value = AuthState.LoginSuccess },
                    onFailure = { _state.value = AuthState.LoginError(Res.string.invalid_credentials) }
                )
            } catch (e: Exception) {
                _state.value = AuthState.LoginError(Res.string.invalid_credentials)
            }
        }
    }

    private fun register(params: RegisterParams, confirmPass: String) {
        if (_state.value is AuthState.Loading) return
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                registerUseCase(params, confirmPass).fold(
                    onSuccess = { _state.value = AuthState.RegisterSuccess },
                    onFailure = { _state.value = AuthState.RegisterError("", Res.string.invalid_credentials) }
                )
            } catch (e: Exception) {
                _state.value = AuthState.RegisterError("", Res.string.invalid_credentials)
            }
        }
    }

    private fun sendCode(emailOrUser: String) {
        if (_state.value is AuthState.Loading) return
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                sendVerificationCodeUseCase(emailOrUser).fold(
                    onSuccess = { _state.value = AuthState.CodeSent(emailOrUser) },
                    onFailure = { _state.value = AuthState.CodeSentError(Res.string.user_not_found) }
                )
            } catch (e: Exception) {
                _state.value = AuthState.CodeSentError(Res.string.user_not_found)
            }
        }
    }

    private fun verifyCode(email: String, code: String) {
        if (_state.value is AuthState.Loading) return
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                verifyCodeUseCase(email, code).fold(
                    onSuccess = { _state.value = AuthState.CodeVerified(email) },
                    onFailure = { _state.value = AuthState.CodeVerifiedError(Res.string.invalid_code) }
                )
            } catch (e: Exception) {
                _state.value = AuthState.CodeVerifiedError(Res.string.invalid_code)
            }
        }
    }

    private fun resetPassword(email: String, newPassword: String) {
        if (_state.value is AuthState.Loading) return
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                resetPasswordUseCase(email, newPassword).fold(
                    onSuccess = { _state.value = AuthState.PasswordResetSuccess },
                    onFailure = { _state.value = AuthState.PasswordResetError(Res.string.invalid_credentials) }
                )
            } catch (e: Exception) {
                _state.value = AuthState.PasswordResetError(Res.string.invalid_credentials)
            }
        }
    }

    private fun validatePassword(password: String) {
        _passwordRequirements.value = PasswordRequirements(
            hasLowercase = password.any { it.isLowerCase() },
            hasUppercase = password.any { it.isUpperCase() },
            hasNumber = password.any { it.isDigit() },
            hasMinLength = password.length >= 8
        )
    }
}
