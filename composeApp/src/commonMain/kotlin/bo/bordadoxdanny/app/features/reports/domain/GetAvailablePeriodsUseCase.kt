package bo.bordadoxdanny.app.features.reports.domain

import kotlinx.coroutines.flow.Flow

class GetAvailablePeriodsUseCase(private val repository: ReportRepository) {
    operator fun invoke(): Flow<List<Period>> {
        return repository.getAvailablePeriods()
    }
}
