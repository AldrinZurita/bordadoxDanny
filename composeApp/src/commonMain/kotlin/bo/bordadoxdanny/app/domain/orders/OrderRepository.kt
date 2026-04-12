package bo.bordadoxdanny.app.domain.orders

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllOrders(): Flow<List<Order>>
    suspend fun saveOrder(order: Order)
}
