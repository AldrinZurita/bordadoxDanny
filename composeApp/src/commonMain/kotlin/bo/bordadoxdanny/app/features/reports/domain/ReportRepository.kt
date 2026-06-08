package bo.bordadoxdanny.app.features.reports.domain

import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getFinancialSummary(period: Period): Flow<FinancialSummary?>
    fun getAvailablePeriods(): Flow<List<Period>>
    fun getAccountsReceivable(): Flow<List<AccountsReceivableItem>>
    fun getAllReports(): Flow<List<Report>>
}
