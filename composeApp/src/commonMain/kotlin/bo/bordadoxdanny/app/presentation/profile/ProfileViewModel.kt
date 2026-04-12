package bo.bordadoxdanny.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.domain.profile.Profile
import bo.bordadoxdanny.app.domain.profile.GetProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(getProfileUseCase: GetProfileUseCase) : ViewModel() {
    val profile: StateFlow<Profile?> = getProfileUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
