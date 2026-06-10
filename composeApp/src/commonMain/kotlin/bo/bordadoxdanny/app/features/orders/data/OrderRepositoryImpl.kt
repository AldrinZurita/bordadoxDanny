package bo.bordadoxdanny.app.features.orders.data

import bo.bordadoxdanny.app.features.orders.domain.Order
import bo.bordadoxdanny.app.features.orders.domain.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(
    private val dao: OrderDao,
    private val syncScheduler: SyncScheduler
) : OrderRepository {
    override fun getAllOrders(userId: String): Flow<List<Order>> = dao.getAllOrders(userId).map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun saveOrder(order: Order): Long {
        val id = dao.insert(order.toEntity())
        syncScheduler.scheduleOrderSync(order.userId)
        return id
    }

    override suspend fun getUniqueCustomerNames(userId: String, query: String): List<String> {
        return dao.getUniqueCustomerNames(userId, query)
    }

    override suspend fun getPendingOrders(userId: String): List<Order> {
        return dao.getPendingOrders(userId).map { it.toDomain() }
    }

    override suspend fun markAsSynced(orderId: Long, userId: String) {
        dao.updateSyncStatus(orderId, userId, "SYNCED")
    }
}
