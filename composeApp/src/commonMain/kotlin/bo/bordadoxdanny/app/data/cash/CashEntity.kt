package bo.bordadoxdanny.app.data.cash

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cash_entries")
data class CashEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val type: String, // "IN" or "OUT"
    val reason: String,
    val timestamp: Long
)
