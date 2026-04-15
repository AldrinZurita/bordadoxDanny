package bo.bordadoxdanny.app.features.cash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.cash.domain.CashEntry
import bo.bordadoxdanny.app.features.cash.domain.GetCashEntriesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CashViewModel(getCashEntriesUseCase: GetCashEntriesUseCase) : ViewModel() {
    val cashEntries: StateFlow<List<CashEntry>> = getCashEntriesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
