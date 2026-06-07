package bo.bordadoxdanny.app.features.profile.presentation

sealed class ProfileIntent {
    data class OnLanguageSelected(val code: String) : ProfileIntent()
    data object OnLogoutClicked : ProfileIntent()
    data object OnLogoutConfirmed : ProfileIntent()
    data object OnLogoutDismissed : ProfileIntent()
}
