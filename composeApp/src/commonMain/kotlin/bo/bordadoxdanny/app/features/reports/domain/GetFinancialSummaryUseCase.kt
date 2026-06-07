package bo.bordadoxdanny.app.features.reports.domain

import kotlinx.coroutines.flow.Flow

class GetFinancialSummaryUseCase(private val repository: ReportRepository) {
    operator fun invoke(period: Period): Flow<FinancialSummary?> {
        return repository.getFinancialSummary(period)
    }
}
