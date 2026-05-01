package bo.bordadoxdanny.app.core.daemon.db

import android.content.Context
import androidx.room.Room

actual fun createDaemonDatabase(context: Any?): DaemonDatabase {
    val ctx = context as Context
    return Room.databaseBuilder<DaemonDatabase>(
        context = ctx,
        name = "daemon_sync_queue.db"
    ).fallbackToDestructiveMigration()
     .build()
}
