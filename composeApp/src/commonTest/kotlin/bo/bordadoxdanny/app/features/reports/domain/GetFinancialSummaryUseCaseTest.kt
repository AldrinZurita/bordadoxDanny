package bo.bordadoxdanny.app.features.reports.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFinancialSummaryUseCaseTest {

    @Test
    fun `given monthly data, net profit equals income minus expenses`() = runTest {
        // GIVEN
        val repo = FakeReportRepository(fakeIncome = 2700.0, fakeExpenses = 450.0)
        val useCase = GetFinancialSummaryUseCase(repo)
        
        // WHEN
        val result = useCase(Period.Month(2026, 2)).first()
        
        // THEN
        assertEquals(2700.0 - 450.0, result?.netProfit)
    }

    class FakeReportRepository(
        private val fakeIncome: Double = 0.0,
        private val fakeExpenses: Double = 0.0
    ) : ReportRepository {
        override fun getFinancialSummary(period: Period): Flow<FinancialSummary?> = flowOf(
            FinancialSummary(
                totalIncome = fakeIncome,
                totalExpenses = fakeExpenses,
                netProfit = fakeIncome - fakeExpenses,
                accountsReceivable = 0.0,
                period = period
            )
        )

        override fun getAvailablePeriods(): Flow<List<Period>> = flowOf(emptyList())
        override fun getAccountsReceivable(): Flow<List<AccountsReceivableItem>> = flowOf(emptyList())
    }
}
