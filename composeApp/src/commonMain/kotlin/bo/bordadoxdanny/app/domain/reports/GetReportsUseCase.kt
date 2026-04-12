package bo.bordadoxdanny.app.domain.reports

import kotlinx.coroutines.flow.Flow

class GetReportsUseCase(private val repository: ReportRepository) {
    operator fun invoke(): Flow<List<Report>> = repository.getAllReports()
}
