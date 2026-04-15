package bo.bordadoxdanny.app.features.orders.domain

data class Order(
    val id: Long = 0,
    val description: String,
    val amount: Double,
    val date: Long
)
