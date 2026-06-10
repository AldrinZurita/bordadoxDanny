package bo.bordadoxdanny.app.features.orders.data

import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: String = "",
    val userId: String = "",
    val customerName: String = "",
    val deliveryDate: Long = 0L,
    val description: String = "",
    val quantity: Int = 0,
    val unitPrice: Double = 0.0,
    val initialPayment: Double = 0.0,
    val total: Double = 0.0,
    val balance: Double = 0.0,
    val status: String = "Pendiente",
    val syncStatus: String = "PENDING",
    val createdAt: Long = 0L
)
