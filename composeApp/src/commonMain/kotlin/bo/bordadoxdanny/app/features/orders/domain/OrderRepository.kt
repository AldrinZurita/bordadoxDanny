package bo.bordadoxdanny.app.features.orders.domain

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllOrders(): Flow<List<Order>>
    suspend fun saveOrder(order: Order): Long
    suspend fun getUniqueCustomerNames(query: String): List<String>
    suspend fun getPendingOrders(): List<Order>
    suspend fun markAsSynced(orderId: Long)
}
