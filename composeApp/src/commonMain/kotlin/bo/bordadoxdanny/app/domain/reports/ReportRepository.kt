package bo.bordadoxdanny.app.domain.reports

import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getAllReports(): Flow<List<Report>>
    suspend fun saveReport(report: Report)
}
