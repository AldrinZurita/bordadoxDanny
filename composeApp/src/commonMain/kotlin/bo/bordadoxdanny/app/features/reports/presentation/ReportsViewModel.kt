package bo.bordadoxdanny.app.features.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.reports.domain.Report
import bo.bordadoxdanny.app.features.reports.domain.GetReportsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ReportsViewModel(getReportsUseCase: GetReportsUseCase) : ViewModel() {
    val reports: StateFlow<List<Report>> = getReportsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
