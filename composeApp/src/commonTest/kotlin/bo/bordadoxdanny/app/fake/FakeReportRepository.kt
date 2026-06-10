package bo.bordadoxdanny.app.fake

import bo.bordadoxdanny.app.features.reports.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeReportRepository(
    private val fakeIncome: Double = 0.0,
    private val fakeExpenses: Double = 0.0,
    private val fakeArItems: List<AccountsReceivableItem> = emptyList(),
    private val fakeReports: List<Report> = emptyList(),
    private val shouldReturnError: Boolean = false
) : ReportRepository {

    override fun getFinancialSummary(userId: String, period: Period): Flow<FinancialSummary?> {
        return if (shouldReturnError) {
            // Flow that throws an exception
            flowOf(null).let { throw Exception("Simulated DB Error") }
        } else {
            flowOf(
                FinancialSummary(
                    totalIncome = fakeIncome,
                    totalExpenses = fakeExpenses,
                    netProfit = fakeIncome - fakeExpenses,
                    accountsReceivable = fakeArItems.sumOf { it.amount },
                    period = period
                )
            )
        }
    }

    override fun getAvailablePeriods(userId: String): Flow<List<Period>> {
        return flowOf(listOf(Period.AllMonths, Period.Month(2026, 6)))
    }

    override fun getAccountsReceivable(userId: String): Flow<List<AccountsReceivableItem>> {
        return flowOf(fakeArItems)
    }

    override fun getAllReports(userId: String): Flow<List<Report>> {
        return flowOf(fakeReports)
    }
}
