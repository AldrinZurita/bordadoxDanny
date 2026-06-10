package bo.bordadoxdanny.app.features.cash.domain

import bo.bordadoxdanny.app.core.domain.SyncStatus

data class Transaction(
    val id: Long = 0,
    val userId: String,
    val amount: Double,
    val type: TransactionType,
    val description: String,
    val reference: String,
    val category: String? = null,
    val timestamp: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

enum class TransactionType {
    INCOME,
    EXPENSE
}
