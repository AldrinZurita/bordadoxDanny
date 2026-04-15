package bo.bordadoxdanny.app.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory
import kotlinx.cinterop.ExperimentalForeignApi

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/bordados.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory = { instantiateImpl() }
    )
}

private fun instantiateImpl(): AppDatabase {
    throw NotImplementedError("Room compiler should generate this")
}
