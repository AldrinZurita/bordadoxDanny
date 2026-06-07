package bo.bordadoxdanny.app.features.profile.presentation

import org.jetbrains.compose.resources.StringResource

sealed class LanguageState {
    data object Loading : LanguageState()
    data class Loaded(val languageCode: String) : LanguageState()
    data class Error(val messageResId: StringResource) : LanguageState()
}
