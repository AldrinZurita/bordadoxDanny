package bo.bordadoxdanny.app.features.orders.presentation.create

sealed class CreateOrderState {
    data object Idle : CreateOrderState()
    data object Loading : CreateOrderState()
    data object Success : CreateOrderState()
    data class Error(val message: String) : CreateOrderState()
}

data class CreateOrderForm(
    val customerName: String = "",
    val deliveryDate: Long? = null,
    val description: String = "",
    val quantity: Int = 1,
    val unitPrice: String = "",
    val initialPayment: String = "",
    val customerNameError: Boolean = false
) {
    val total: Double = quantity * (unitPrice.toDoubleOrNull() ?: 0.0)
    val balance: Double = total - (initialPayment.toDoubleOrNull() ?: 0.0)
}
