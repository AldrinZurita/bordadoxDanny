package bo.bordadoxdanny.app.features.reports.domain

import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getFinancialSummary(userId: String, period: Period): Flow<FinancialSummary?>
    fun getAvailablePeriods(userId: String): Flow<List<Period>>
    fun getAccountsReceivable(userId: String): Flow<List<AccountsReceivableItem>>
    fun getAllReports(userId: String): Flow<List<Report>>
}
