package bo.bordadoxdanny.app.data.reports

import bo.bordadoxdanny.app.domain.reports.Report
import bo.bordadoxdanny.app.domain.reports.ReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReportRepositoryImpl(private val dao: ReportDao) : ReportRepository {
    override fun getAllReports(): Flow<List<Report>> = dao.getAllReports().map { entities ->
        entities.map { Report(it.id, it.title, it.content, it.createdDate) }
    }

    override suspend fun saveReport(report: Report) {
        dao.insert(ReportEntity(title = report.title, content = report.content, createdDate = report.createdDate))
    }
}
