package bo.bordadoxdanny.app.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.data.preferences.PreferencesRepository
import bo.bordadoxdanny.app.core.locale.LocaleManager
import bo.bordadoxdanny.app.features.profile.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val localeManager: LocaleManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<LanguageState>(LanguageState.Loading)
    val state: StateFlow<LanguageState> = _state.asStateFlow()

    init {
        onIntent(LanguageIntent.OnLoadLanguage)
    }

    fun onIntent(intent: LanguageIntent) {
        when (intent) {
            is LanguageIntent.OnLoadLanguage -> loadLanguage()
            is LanguageIntent.OnLanguageChanged -> changeLanguage(intent.code)
        }
    }

    private fun loadLanguage() {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser().filterNotNull().first()
                val lang = preferencesRepository.getLanguage(user.id)
                val code = lang ?: "en"
                localeManager.applyLocale(code)
                _state.value = LanguageState.Loaded(code)
            } catch (e: Exception) {
                _state.value = LanguageState.Loaded("en")
            }
        }
    }

    private fun changeLanguage(code: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser().filterNotNull().first()
                preferencesRepository.saveLanguage(user.id, code)
                localeManager.applyLocale(code)
                _state.value = LanguageState.Loaded(code)
            } catch (e: Exception) {
                _state.value = LanguageState.Loaded(code)
            }
        }
    }
}
