package bo.bordadoxdanny.app.features.cash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.cash.domain.GetTotalBalanceUseCase
import bo.bordadoxdanny.app.features.cash.domain.GetTransactionsUseCase
import bo.bordadoxdanny.app.features.cash.domain.Transaction
import bo.bordadoxdanny.app.features.cash.domain.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

data class CashState(
    val selectedType: TransactionType = TransactionType.INCOME,
    val transactions: List<Transaction> = emptyList(),
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false
)

class CashViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getTotalBalanceUseCase: GetTotalBalanceUseCase
) : ViewModel() {

    private val _selectedType = MutableStateFlow(TransactionType.INCOME)
    val selectedType = _selectedType.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<CashState> = _selectedType.flatMapLatest { type ->
        combine(
            getTransactionsUseCase(type),
            getTotalBalanceUseCase(type)
        ) { transactions, total ->
            CashState(
                selectedType = type,
                transactions = transactions,
                totalAmount = total,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CashState(isLoading = true)
    )

    fun selectType(type: TransactionType) {
        _selectedType.value = type
    }
}
