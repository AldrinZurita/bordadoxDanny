package bo.bordadoxdanny.app.features.orders.domain

class CreateOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order): Long {
        // La lógica de cálculo de saldo se refuerza aquí por seguridad, 
        // aunque el ViewModel ya la realice para la UI.
        val finalOrder = order.copy(
            total = order.quantity * order.unitPrice,
            balance = (order.quantity * order.unitPrice) - order.initialPayment
        )
        return repository.saveOrder(finalOrder)
    }
}
