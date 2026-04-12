package bo.bordadoxdanny.app.data.orders

import bo.bordadoxdanny.app.domain.orders.Order
import bo.bordadoxdanny.app.domain.orders.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(private val dao: OrderDao) : OrderRepository {
    override fun getAllOrders(): Flow<List<Order>> = dao.getAllOrders().map { entities ->
        entities.map { Order(it.id, it.description, it.amount, it.date) }
    }

    override suspend fun saveOrder(order: Order) {
        dao.insert(OrderEntity(description = order.description, amount = order.amount, date = order.date))
    }
}
