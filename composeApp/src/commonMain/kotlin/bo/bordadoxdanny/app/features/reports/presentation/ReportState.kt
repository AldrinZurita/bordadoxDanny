package bo.bordadoxdanny.app.features.reports.presentation

import bo.bordadoxdanny.app.features.reports.domain.AccountsReceivableItem
import bo.bordadoxdanny.app.features.reports.domain.FinancialSummary
import bo.bordadoxdanny.app.features.reports.domain.Period
import org.jetbrains.compose.resources.StringResource

sealed class ReportState {
    data object Loading : ReportState()
    data class Success(
        val summary: FinancialSummary,
        val arItems: List<AccountsReceivableItem>,
        val mockChartData: List<ChartDataPoint>,
        val periods: List<Period>,
        val selectedPeriod: Period
    ) : ReportState()
    data class Error(val messageResId: StringResource) : ReportState()
}
