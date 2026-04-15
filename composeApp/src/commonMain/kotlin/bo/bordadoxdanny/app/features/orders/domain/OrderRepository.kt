package bo.bordadoxdanny.app.features.orders.domain

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllOrders(): Flow<List<Order>>
    suspend fun saveOrder(order: Order)
}
