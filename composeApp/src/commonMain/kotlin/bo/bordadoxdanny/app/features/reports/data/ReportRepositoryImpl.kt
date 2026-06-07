package bo.bordadoxdanny.app.features.reports.data

import bo.bordadoxdanny.app.features.reports.domain.AccountsReceivableItem
import bo.bordadoxdanny.app.features.reports.domain.FinancialSummary
import bo.bordadoxdanny.app.features.reports.domain.Period
import bo.bordadoxdanny.app.features.reports.domain.ReportRepository
import bo.bordadoxdanny.app.features.orders.data.OrderDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReportRepositoryImpl(
    private val reportSummaryDao: ReportSummaryDao,
    private val orderDao: OrderDao
) : ReportRepository {

    override fun getFinancialSummary(period: Period): Flow<FinancialSummary?> {
        return reportSummaryDao.getSummaryById(period.id).map { it?.toDomain() }
    }

    override fun getAvailablePeriods(): Flow<List<Period>> {
        return reportSummaryDao.getAllSummaries().map { summaries ->
            summaries.map { entity ->
                when {
                    entity.year == null && entity.month == null -> Period.AllMonths
                    entity.month == null -> Period.Year(entity.year!!)
                    else -> Period.Month(entity.year!!, entity.month!!)
                }
            }
        }
    }

    override fun getAccountsReceivable(): Flow<List<AccountsReceivableItem>> {
        // En un escenario real, esto vendría de una tabla de pagos/deudas o filtrando órdenes.
        // Según el prompt: "suma de órdenes con estado pendiente de pago"
        return orderDao.getAllOrders().map { orders ->
            orders.filter { it.balance > 0 }.map { order ->
                AccountsReceivableItem(
                    id = order.id.toString(),
                    clientName = order.customerName,
                    description = order.description,
                    amount = order.balance,
                    isUrgent = order.balance > 1000 // Ejemplo de lógica de urgencia
                )
            }
        }
    }
}
