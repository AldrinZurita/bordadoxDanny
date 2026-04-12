package bo.bordadoxdanny.app.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.domain.reports.Report
import bo.bordadoxdanny.app.domain.reports.GetReportsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ReportsViewModel(getReportsUseCase: GetReportsUseCase) : ViewModel() {
    val reports: StateFlow<List<Report>> = getReportsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
