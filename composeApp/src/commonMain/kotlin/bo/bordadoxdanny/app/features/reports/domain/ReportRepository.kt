package bo.bordadoxdanny.app.features.reports.domain

import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getAllReports(): Flow<List<Report>>
    suspend fun saveReport(report: Report)
}
