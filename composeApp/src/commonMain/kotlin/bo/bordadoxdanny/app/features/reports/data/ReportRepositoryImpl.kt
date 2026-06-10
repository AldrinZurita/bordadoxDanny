package bo.bordadoxdanny.app.features.reports.data

import bo.bordadoxdanny.app.features.reports.domain.AccountsReceivableItem
import bo.bordadoxdanny.app.features.reports.domain.FinancialSummary
import bo.bordadoxdanny.app.features.reports.domain.Period
import bo.bordadoxdanny.app.features.reports.domain.Report
import bo.bordadoxdanny.app.features.reports.domain.ReportRepository
import bo.bordadoxdanny.app.features.orders.data.OrderDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReportRepositoryImpl(
    private val reportSummaryDao: ReportSummaryDao,
    private val orderDao: OrderDao,
    private val reportDao: ReportDao
) : ReportRepository {

    override fun getFinancialSummary(userId: String, period: Period): Flow<FinancialSummary?> {
        return reportSummaryDao.getSummaryById(period.id, userId).map { it?.toDomain() }
    }

    override fun getAvailablePeriods(userId: String): Flow<List<Period>> {
        return reportSummaryDao.getAllSummaries(userId).map { summaries ->
            if (summaries.isEmpty()) {
                listOf(Period.AllMonths)
            } else {
                summaries.map { entity ->
                    when {
                        entity.periodId == "ALL" -> Period.AllMonths
                        entity.month == null -> Period.Year(entity.year ?: 2024)
                        else -> Period.Month(entity.year ?: 2024, entity.month)
                    }
                }
            }
        }
    }

    override fun getAccountsReceivable(userId: String): Flow<List<AccountsReceivableItem>> {
        return orderDao.getAllOrders(userId).map { orders ->
            orders.filter { it.balance > 0 }.map { order ->
                AccountsReceivableItem(
                    id = order.id.toString(),
                    clientName = order.customerName,
                    description = order.description,
                    amount = order.balance,
                    isUrgent = order.balance > 1000
                )
            }
        }
    }

    override fun getAllReports(userId: String): Flow<List<Report>> {
        return reportDao.getAllReports(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
