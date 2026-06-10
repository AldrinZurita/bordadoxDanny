package bo.bordadoxdanny.app.features.cash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.cash.domain.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*

/**
 * Representa los parámetros intermedios para la combinación de flujos.
 */
private data class FilterAndUiParams(
    val type: TransactionType,
    val month: String?,
    val availableMonths: List<String>,
    val showIncome: Boolean,
    val showExpense: Boolean,
    val showMonthSelect: Boolean
)

data class CashState(
    val selectedType: TransactionType = TransactionType.INCOME,
    val selectedMonth: String? = null, // null significa "All Months"
    val availableMonths: List<String> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val showAddIncomeSheet: Boolean = false,
    val showAddExpenseSheet: Boolean = false,
    val showMonthSelector: Boolean = false
)

class CashViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getTotalBalanceUseCase: GetTotalBalanceUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val repository: TransactionRepository // Para obtener los timestamps del filtro
) : ViewModel() {

    private val _selectedType = MutableStateFlow(TransactionType.INCOME)
    private val _selectedMonth = MutableStateFlow<String?>(null)
    private val _showAddIncomeSheet = MutableStateFlow(false)
    private val _showAddExpenseSheet = MutableStateFlow(false)
    private val _showMonthSelector = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<CashState> = combine(
        _selectedType,
        _selectedMonth,
        _showAddIncomeSheet,
        _showAddExpenseSheet,
        _showMonthSelector,
        repository.getAllTransactionTimestamps()
    ) { args: Array<Any?> ->
        // Extracción segura de tipos para evitar errores de inferencia del compilador
        val type = args[0] as TransactionType
        val month = args[1] as? String
        val showIncome = args[2] as Boolean
        val showExpense = args[3] as Boolean
        val showMonthSelect = args[4] as Boolean
        val timestamps = args[5] as List<Long>

        val availableMonths = generateAvailableMonths(timestamps)

        FilterAndUiParams(type, month, availableMonths, showIncome, showExpense, showMonthSelect)
    }.flatMapLatest { params ->
        getTransactionsUseCase(params.type).map { allTransactions ->
            // Filtrado reactivo en memoria para máxima fluidez en KMP
            val filtered = if (params.month == null) {
                allTransactions
            } else {
                allTransactions.filter { formatMonthYear(it.timestamp) == params.month }
            }

            CashState(
                selectedType = params.type,
                selectedMonth = params.month,
                availableMonths = params.availableMonths,
                transactions = filtered,
                totalAmount = filtered.sumOf { it.amount },
                isLoading = false,
                showAddIncomeSheet = params.showIncome,
                showAddExpenseSheet = params.showExpense,
                showMonthSelector = params.showMonthSelect
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

    fun selectMonth(month: String?) {
        _selectedMonth.value = month
        _showMonthSelector.value = false
    }

    fun toggleMonthSelector(show: Boolean) {
        _showMonthSelector.value = show
    }

    fun showAddIncome(show: Boolean) {
        _showAddIncomeSheet.value = show
    }

    fun showAddExpense(show: Boolean) {
        _showAddExpenseSheet.value = show
    }

    fun saveTransaction(
        amount: Double,
        description: String,
        type: TransactionType,
        category: String? = null
    ) {
        viewModelScope.launch {
            val transaction = Transaction(
                amount = amount,
                type = type,
                description = description,
                reference = "", // Entrada manual
                category = category,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
            addTransactionUseCase(transaction)
            if (type == TransactionType.INCOME) _showAddIncomeSheet.value = false
            else _showAddExpenseSheet.value = false
        }
    }

    private fun generateAvailableMonths(timestamps: List<Long>): List<String> {
        val months = timestamps.map { formatMonthYear(it) }.distinct().toMutableList()
        val currentMonth = formatMonthYear(Clock.System.now().toEpochMilliseconds())
        if (!months.contains(currentMonth)) {
            months.add(0, currentMonth)
        }
        return months
    }

    private fun formatMonthYear(timestamp: Long): String {
        val date = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
        // Formato: "June 2026"
        return "${date.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${date.year}"
    }
}
