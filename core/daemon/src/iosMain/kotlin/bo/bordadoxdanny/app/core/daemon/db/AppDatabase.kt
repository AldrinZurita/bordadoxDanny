package bo.bordadoxdanny.app.core.daemon.db

import androidx.room.Room
import platform.Foundation.NSHomeDirectory

actual fun createDaemonDatabase(context: Any?): DaemonDatabase {
    val dbFile = NSHomeDirectory() + "/daemon_db.db"
    return Room.databaseBuilder<DaemonDatabase>(
        name = dbFile,
        factory = { DaemonDatabase::class.instantiateImpl() }
    ).build()
}