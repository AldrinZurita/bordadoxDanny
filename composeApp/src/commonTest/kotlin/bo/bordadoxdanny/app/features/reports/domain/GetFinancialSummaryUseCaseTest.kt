package bo.bordadoxdanny.app.features.reports.domain

import bo.bordadoxdanny.app.fake.FakeReportRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GetFinancialSummaryUseCaseTest {

    @Test
    fun `given monthly data, net profit equals income minus expenses`() = runTest {
        // GIVEN
        val repo = FakeReportRepository(fakeIncome = 2700.0, fakeExpenses = 450.0)
        val useCase = GetFinancialSummaryUseCase(repo)
        
        // WHEN
        val result = useCase(Period.Month(2026, 2)).first()
        
        // THEN
        assertNotNull(result)
        assertEquals(2700.0 - 450.0, result.netProfit, 0.001)
    }

    @Test
    fun `given no data, all summary values are zero`() = runTest {
        val repo = FakeReportRepository(fakeIncome = 0.0, fakeExpenses = 0.0)
        val useCase = GetFinancialSummaryUseCase(repo)

        val result = useCase(Period.AllMonths).first()

        assertNotNull(result)
        assertEquals(0.0, result.totalIncome, 0.001)
        assertEquals(0.0, result.totalExpenses, 0.001)
        assertEquals(0.0, result.netProfit, 0.001)
    }

    @Test
    fun `given AllMonths period, use case passes it to repository correctly`() = runTest {
        val repo = FakeReportRepository(fakeIncome = 1500.0, fakeExpenses = 300.0)
        val useCase = GetFinancialSummaryUseCase(repo)

        val result = useCase(Period.AllMonths).first()

        assertNotNull(result)
        assertEquals(Period.AllMonths, result.period)
    }
}
