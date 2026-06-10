package bo.bordadoxdanny.app.features.orders.domain

data class Order(
    val id: Long = 0,
    val userId: String,
    val customerName: String,
    val deliveryDate: Long,
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
    val initialPayment: Double,
    val total: Double,
    val balance: Double,
    val status: String = "Pendiente",
    val syncStatus: String = "PENDING",
    val createdAt: Long = 0
)
