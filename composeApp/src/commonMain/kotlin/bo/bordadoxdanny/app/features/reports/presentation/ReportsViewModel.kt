package bo.bordadoxdanny.app.features.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.reports.domain.Report
import bo.bordadoxdanny.app.features.reports.domain.GetReportsUseCase
import bo.bordadoxdanny.app.features.profile.domain.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class ReportsViewModel(
    private val getReportsUseCase: GetReportsUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val reports: StateFlow<List<Report>> = authRepository.getCurrentUser()
        .filterNotNull()
        .flatMapLatest { user -> 
            getReportsUseCase(user.id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
