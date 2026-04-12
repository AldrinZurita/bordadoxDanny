package bo.bordadoxdanny.app.domain.orders

import kotlinx.coroutines.flow.Flow

class GetOrdersUseCase(private val repository: OrderRepository) {
    operator fun invoke(): Flow<List<Order>> = repository.getAllOrders()
}
