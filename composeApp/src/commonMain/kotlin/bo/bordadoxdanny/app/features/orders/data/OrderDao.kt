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

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllOrders(userId: String): Flow<List<OrderEntity>>

    @Query("SELECT DISTINCT customerName FROM orders WHERE userId = :userId AND customerName LIKE '%' || :query || '%'")
    suspend fun getUniqueCustomerNames(userId: String, query: String): List<String>
    
    @Query("SELECT MAX(id) FROM orders WHERE userId = :userId")
    suspend fun getLatestOrderId(userId: String): Long?

    @Query("SELECT * FROM orders WHERE userId = :userId AND syncStatus = 'PENDING'")
    suspend fun getPendingOrders(userId: String): List<OrderEntity>

    @Query("UPDATE orders SET syncStatus = :status WHERE id = :orderId AND userId = :userId")
    suspend fun updateSyncStatus(orderId: Long, userId: String, status: String)
}
