package bo.bordadoxdanny.app.features.cash.domain

import bo.bordadoxdanny.app.core.domain.SyncStatus

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val description: String,
    val reference: String, // e.g. "Nota #1029"
    val category: String? = null, // Material, Hilos, etc.
    val timestamp: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

enum class TransactionType {
    INCOME,
    EXPENSE
}
