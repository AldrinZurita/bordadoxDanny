package bo.bordadoxdanny.app.features.cash.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import bo.bordadoxdanny.app.core.domain.SyncStatus

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val type: String, // "INCOME", "EXPENSE"
    val description: String,
    val reference: String,
    val category: String? = null,
    val timestamp: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)
