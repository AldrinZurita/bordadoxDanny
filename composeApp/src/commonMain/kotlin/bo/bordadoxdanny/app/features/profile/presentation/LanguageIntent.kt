package bo.bordadoxdanny.app.features.profile.presentation

sealed class LanguageIntent {
    data class OnLanguageChanged(val code: String) : LanguageIntent()
    data object OnLoadLanguage : LanguageIntent()
}
