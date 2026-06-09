package bo.bordadoxdanny.app.features.orders.domain

import bo.bordadoxdanny.app.features.cash.domain.Transaction
import bo.bordadoxdanny.app.features.cash.domain.TransactionRepository
import bo.bordadoxdanny.app.features.cash.domain.TransactionType
import kotlinx.datetime.Clock

class CreateOrderUseCase(
    private val orderRepository: OrderRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(order: Order): Long {
        val finalOrder = order.copy(
            total = order.quantity * order.unitPrice,
            balance = (order.quantity * order.unitPrice) - order.initialPayment
        )
        
        val orderId = orderRepository.saveOrder(finalOrder)
        
        if (finalOrder.initialPayment > 0) {
            transactionRepository.addTransaction(
                Transaction(
                    amount = finalOrder.initialPayment,
                    type = TransactionType.INCOME,
                    description = "Pago Inicial – Nota #$orderId",
                    reference = "Nota #$orderId",
                    timestamp = Clock.System.now().toEpochMilliseconds()
                )
            )
        }
        
        return orderId
    }
}
