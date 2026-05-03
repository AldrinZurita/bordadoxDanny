package bo.bordadoxdanny.app.core.daemon.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SyncQueueDao {
    @Insert
    suspend fun insertAll(items: List<SyncQueueEntity>)

    @Query("SELECT * FROM sync_queue WHERE status = :status")
    suspend fun getByStatus(status: String): List<SyncQueueEntity>

    @Query("UPDATE sync_queue SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    @Query("DELETE FROM sync_queue WHERE status = 'COMPLETED'")
    suspend fun deleteCompleted()

    @Query("UPDATE sync_queue SET status = 'PENDING', retryCount = 0 WHERE status = 'FAILED'")
    suspend fun resetFailedToRetry()
}
