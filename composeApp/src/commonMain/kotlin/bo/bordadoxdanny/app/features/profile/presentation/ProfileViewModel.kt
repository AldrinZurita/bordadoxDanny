package bo.bordadoxdanny.app.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.profile.domain.Profile
import bo.bordadoxdanny.app.features.profile.domain.GetProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(getProfileUseCase: GetProfileUseCase) : ViewModel() {
    val profile: StateFlow<Profile?> = getProfileUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
