package bo.bordadoxdanny.app.core.daemon.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SyncQueueEntity::class], version = 1)
abstract class DaemonDatabase : RoomDatabase() {
    abstract fun syncQueueDao(): SyncQueueDao
}

expect fun createDaemonDatabase(context: Any?): DaemonDatabase
