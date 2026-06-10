package bo.bordadoxdanny.app.features.orders.domain

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllOrders(userId: String): Flow<List<Order>>
    suspend fun saveOrder(order: Order): Long
    suspend fun getUniqueCustomerNames(userId: String, query: String): List<String>
    suspend fun getPendingOrders(userId: String): List<Order>
    suspend fun markAsSynced(orderId: Long, userId: String)
}
