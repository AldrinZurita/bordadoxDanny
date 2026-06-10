package bo.bordadoxdanny.app.features.orders.domain

import kotlinx.coroutines.flow.Flow

class GetOrdersUseCase(private val repository: OrderRepository) {
    operator fun invoke(userId: String): Flow<List<Order>> =
        repository.getAllOrders(userId)
}
