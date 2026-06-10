package bo.bordadoxdanny.app.features.reports.presentation

import bo.bordadoxdanny.app.fake.FakeAuthRepository
import bo.bordadoxdanny.app.fake.FakeRemoteConfigManager
import bo.bordadoxdanny.app.fake.FakeReportRepository
import bo.bordadoxdanny.app.features.reports.domain.GetAccountsReceivableUseCase
import bo.bordadoxdanny.app.features.reports.domain.GetAvailablePeriodsUseCase
import bo.bordadoxdanny.app.features.reports.domain.GetFinancialSummaryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ReportViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeAuthRepository = FakeAuthRepository()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given repository returns data, state transitions to Success`() = runTest {
        val repo = FakeReportRepository(fakeIncome = 1500.0, fakeExpenses = 600.0)
        val vm = ReportViewModel(
            getFinancialSummaryUseCase = GetFinancialSummaryUseCase(repo),
            getAvailablePeriodsUseCase = GetAvailablePeriodsUseCase(repo),
            getAccountsReceivableUseCase = GetAccountsReceivableUseCase(repo),
            remoteConfigManager = FakeRemoteConfigManager(),
            authRepository = fakeAuthRepository
        )

        assertTrue(vm.state.value is ReportState.Success)
        val success = vm.state.value as ReportState.Success
        assertEquals(900.0, success.summary.netProfit, 0.001)
    }

    @Test
    fun `given OnOpenPeriodSelector intent, showPeriodSelector becomes true`() = runTest {
        val repo = FakeReportRepository()
        val vm = ReportViewModel(
            getFinancialSummaryUseCase = GetFinancialSummaryUseCase(repo),
            getAvailablePeriodsUseCase = GetAvailablePeriodsUseCase(repo),
            getAccountsReceivableUseCase = GetAccountsReceivableUseCase(repo),
            remoteConfigManager = FakeRemoteConfigManager(),
            authRepository = fakeAuthRepository
        )

        vm.onIntent(ReportIntent.OnOpenPeriodSelector)

        assertTrue(vm.showPeriodSelector.value)
    }

    @Test
    fun `given OnClosePeriodSelector intent, showPeriodSelector becomes false`() = runTest {
        val repo = FakeReportRepository()
        val vm = ReportViewModel(
            getFinancialSummaryUseCase = GetFinancialSummaryUseCase(repo),
            getAvailablePeriodsUseCase = GetAvailablePeriodsUseCase(repo),
            getAccountsReceivableUseCase = GetAccountsReceivableUseCase(repo),
            remoteConfigManager = FakeRemoteConfigManager(),
            authRepository = fakeAuthRepository
        )

        vm.onIntent(ReportIntent.OnOpenPeriodSelector)
        vm.onIntent(ReportIntent.OnClosePeriodSelector)

        assertFalse(vm.showPeriodSelector.value)
    }

    @Test
    fun `mock chart data in ViewModel contains exactly 6 entries`() = runTest {
        val repo = FakeReportRepository()
        val vm = ReportViewModel(
            getFinancialSummaryUseCase = GetFinancialSummaryUseCase(repo),
            getAvailablePeriodsUseCase = GetAvailablePeriodsUseCase(repo),
            getAccountsReceivableUseCase = GetAccountsReceivableUseCase(repo),
            remoteConfigManager = FakeRemoteConfigManager(),
            authRepository = fakeAuthRepository
        )

        val success = vm.state.value as? ReportState.Success
        assertNotNull(success)
        assertEquals(6, success.mockChartData.size)
    }
}
