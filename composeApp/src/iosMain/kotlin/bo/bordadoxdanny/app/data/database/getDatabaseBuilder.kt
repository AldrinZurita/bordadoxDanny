package bo.bordadoxdanny.app.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/bordados.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory = { 
            @Suppress("UNRESOLVED_REFERENCE")
            AppDatabaseConstructor.initialize() 
        }
    )
}
