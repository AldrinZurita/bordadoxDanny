package bo.bordadoxdanny.app.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import bo.bordadoxdanny.app.ContextProvider

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context = ContextProvider.getContext()
    val dbFile = context.getDatabasePath("bordados_db.db")
    
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath,
        factory = { AppDatabaseConstructor.initialize() }
    )
}
