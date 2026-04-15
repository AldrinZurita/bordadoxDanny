package bo.bordadoxdanny.app.features.cash.domain

data class CashEntry(
    val id: Long = 0,
    val amount: Double,
    val type: String,
    val reason: String,
    val timestamp: Long
)
