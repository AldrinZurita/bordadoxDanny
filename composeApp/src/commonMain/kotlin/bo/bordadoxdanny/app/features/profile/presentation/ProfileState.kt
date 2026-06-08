package bo.bordadoxdanny.app.features.profile.presentation

import bo.bordadoxdanny.app.features.profile.domain.User
import org.jetbrains.compose.resources.StringResource

data class AppLanguage(val code: String, val displayName: String)

sealed class ProfileState {
    data object Loading : ProfileState()
    data class Success(
        val user: User,
        val availableLanguages: List<AppLanguage>
    ) : ProfileState()
    data class Error(val messageResId: StringResource) : ProfileState()
    data object LoggedOut : ProfileState()
}
