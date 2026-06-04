package bo.bordadoxdanny.app.features.orders.data

import bo.bordadoxdanny.app.features.orders.domain.Order
import bo.bordadoxdanny.app.features.orders.domain.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(
    private val dao: OrderDao,
    private val syncScheduler: SyncScheduler
) : OrderRepository {
    override fun getAllOrders(): Flow<List<Order>> = dao.getAllOrders().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun saveOrder(order: Order): Long {
        val id = dao.insert(order.toEntity())
        syncScheduler.scheduleOrderSync()
        return id
    }

    override suspend fun getUniqueCustomerNames(query: String): List<String> {
        return dao.getUniqueCustomerNames(query)
    }

    override suspend fun getPendingOrders(): List<Order> {
        return dao.getPendingOrders().map { it.toDomain() }
    }

    override suspend fun markAsSynced(orderId: Long) {
        dao.updateSyncStatus(orderId, "SYNCED")
    }
}
