package bo.bordadoxdanny.app.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.data.preferences.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val preferencesRepository: PreferencesRepository
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
                val lang = preferencesRepository.getLanguage()
                if (lang != null) {
                    _state.value = LanguageState.Loaded(lang)
                } else {
                    _state.value = LanguageState.Loaded("en")
                }
            } catch (e: Exception) {
                _state.value = LanguageState.Loaded("en")
            }
        }
    }

    private fun changeLanguage(code: String) {
        viewModelScope.launch {
            try {
                preferencesRepository.saveLanguage(code)
                _state.value = LanguageState.Loaded(code)
            } catch (e: Exception) {
                _state.value = LanguageState.Loaded(code)
            }
        }
    }
}
