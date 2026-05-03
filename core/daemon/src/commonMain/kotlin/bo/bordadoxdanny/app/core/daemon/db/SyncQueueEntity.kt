package bo.bordadoxdanny.app.core.daemon.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val payload: String,
    val retryCount: Int = 0,
    val createdAt: Long,
    val status: String
)
