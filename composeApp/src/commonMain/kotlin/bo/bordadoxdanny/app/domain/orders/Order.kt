package bo.bordadoxdanny.app.domain.orders

data class Order(
    val id: Long = 0,
    val description: String,
    val amount: Double,
    val date: Long
)
