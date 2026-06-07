package bo.bordadoxdanny.app.features.reports.presentation

import bo.bordadoxdanny.app.features.reports.domain.Period

sealed class ReportIntent {
    data class OnPeriodSelected(val period: Period) : ReportIntent()
    data object OnRefresh : ReportIntent()
    data object OnOpenPeriodSelector : ReportIntent()
    data object OnClosePeriodSelector : ReportIntent()
}
