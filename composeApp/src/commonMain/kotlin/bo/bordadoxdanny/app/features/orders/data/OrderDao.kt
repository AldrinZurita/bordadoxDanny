package bo.bordadoxdanny.app.features.orders.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity): Long

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT DISTINCT customerName FROM orders WHERE customerName LIKE '%' || :query || '%'")
    suspend fun getUniqueCustomerNames(query: String): List<String>
    
    @Query("SELECT MAX(id) FROM orders")
    suspend fun getLatestOrderId(): Long?

    @Query("SELECT * FROM orders WHERE syncStatus = 'PENDING'")
    suspend fun getPendingOrders(): List<OrderEntity>

    @Query("UPDATE orders SET syncStatus = :status WHERE id = :orderId")
    suspend fun updateSyncStatus(orderId: Long, status: String)
}
