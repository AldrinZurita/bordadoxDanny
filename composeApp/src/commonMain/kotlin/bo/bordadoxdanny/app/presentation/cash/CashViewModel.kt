package bo.bordadoxdanny.app.presentation.cash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.domain.cash.CashEntry
import bo.bordadoxdanny.app.domain.cash.GetCashEntriesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CashViewModel(getCashEntriesUseCase: GetCashEntriesUseCase) : ViewModel() {
    val cashEntries: StateFlow<List<CashEntry>> = getCashEntriesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
