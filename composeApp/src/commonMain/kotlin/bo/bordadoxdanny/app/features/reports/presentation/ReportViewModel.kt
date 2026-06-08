package bo.bordadoxdanny.app.features.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.reports.domain.*
import bo.bordadoxdanny.app.firebase.RemoteConfigManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.no_data_for_period

class ReportViewModel(
    private val getFinancialSummaryUseCase: GetFinancialSummaryUseCase,
    private val getAvailablePeriodsUseCase: GetAvailablePeriodsUseCase,
    private val getAccountsReceivableUseCase: GetAccountsReceivableUseCase,
    private val remoteConfigManager: RemoteConfigManager
) : ViewModel() {

    private val _state = MutableStateFlow<ReportState>(ReportState.Loading)
    val state: StateFlow<ReportState> = _state.asStateFlow()

    private val _showPeriodSelector = MutableStateFlow(false)
    val showPeriodSelector: StateFlow<Boolean> = _showPeriodSelector.asStateFlow()

    private val _selectedPeriod = MutableStateFlow<Period>(Period.AllMonths)

    private val mockChartData = listOf(
        ChartDataPoint("Jan", 1500.0, 600.0),
        ChartDataPoint("Feb", 2700.0, 450.0),
        ChartDataPoint("Mar", 0.0, 0.0),
        ChartDataPoint("Apr", 0.0, 0.0),
        ChartDataPoint("May", 0.0, 0.0),
        ChartDataPoint("Jun", 0.0, 0.0),
    )

    init {
        loadData()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadData() {
        viewModelScope.launch {
            combine(
                _selectedPeriod.flatMapLatest { getFinancialSummaryUseCase(it) },
                getAvailablePeriodsUseCase(),
                getAccountsReceivableUseCase()
            ) { summary, periods, arItems ->
                if (summary != null) {
                    val filteredArItems = if (remoteConfigManager.getShowAccountsReceivable()) {
                        arItems
                    } else {
                        emptyList()
                    }
                    ReportState.Success(
                        summary = summary,
                        arItems = filteredArItems,
                        mockChartData = mockChartData,
                        periods = periods.ifEmpty { listOf(Period.AllMonths) },
                        selectedPeriod = _selectedPeriod.value
                    )
                } else {
                    ReportState.Success(
                        summary = FinancialSummary(0.0, 0.0, 0.0, 0.0, _selectedPeriod.value),
                        arItems = emptyList(),
                        mockChartData = mockChartData,
                        periods = periods.ifEmpty { listOf(Period.AllMonths) },
                        selectedPeriod = _selectedPeriod.value
                    )
                }
            }.map { it as ReportState }
                .catch {
                    emit(ReportState.Error(Res.string.no_data_for_period))
                }.collect {
                    _state.value = it
                }
        }
    }

    fun onIntent(intent: ReportIntent) {
        when (intent) {
            is ReportIntent.OnPeriodSelected -> {
                _selectedPeriod.value = intent.period
                _showPeriodSelector.value = false
            }
            ReportIntent.OnRefresh -> loadData()
            ReportIntent.OnOpenPeriodSelector -> _showPeriodSelector.value = true
            ReportIntent.OnClosePeriodSelector -> _showPeriodSelector.value = false
        }
    }
}
