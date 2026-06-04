package bo.bordadoxdanny.app.features.orders.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
