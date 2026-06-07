package bo.bordadoxdanny.app.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.profile.domain.GetCurrentUserUseCase
import bo.bordadoxdanny.app.features.profile.domain.LogoutUseCase
import bo.bordadoxdanny.app.features.profile.domain.UpdateLanguageUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog.asStateFlow()

    // Available languages based on strings.xml support (English, Spanish, French)
    private val availableLanguages = listOf(
        AppLanguage("en", "English"),
        AppLanguage("es", "Español"),
        AppLanguage("fr", "Français")
    )

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                if (user != null) {
                    _state.value = ProfileState.Success(
                        user = user,
                        availableLanguages = availableLanguages
                    )
                } else {
                    _state.value = ProfileState.LoggedOut
                }
            }
        }
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.OnLanguageSelected -> {
                val currentState = _state.value
                if (currentState is ProfileState.Success) {
                    viewModelScope.launch {
                        updateLanguageUseCase(currentState.user.id, intent.code)
                    }
                }
            }
            ProfileIntent.OnLogoutClicked -> {
                _showLogoutDialog.value = true
            }
            ProfileIntent.OnLogoutConfirmed -> {
                _showLogoutDialog.value = false
                viewModelScope.launch {
                    logoutUseCase()
                    _state.value = ProfileState.LoggedOut
                }
            }
            ProfileIntent.OnLogoutDismissed -> {
                _showLogoutDialog.value = false
            }
        }
    }
}
