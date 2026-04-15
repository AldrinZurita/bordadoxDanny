package bo.bordadoxdanny.app.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = bo.bordadoxdanny.app.ContextProvider.getContext()
    val dbFile = appContext.getDatabasePath("bordados.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
