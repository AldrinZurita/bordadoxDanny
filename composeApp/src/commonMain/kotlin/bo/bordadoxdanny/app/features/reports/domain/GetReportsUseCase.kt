package bo.bordadoxdanny.app.features.reports.domain

import kotlinx.coroutines.flow.Flow

class GetReportsUseCase(private val repository: ReportRepository) {
    operator fun invoke(userId: String): Flow<List<Report>> = repository.getAllReports(userId)
}
