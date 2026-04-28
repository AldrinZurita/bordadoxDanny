package bo.bordadoxdanny.app.data.database

@Suppress(names = ["NO_ACTUAL_FOR_EXPECT"])
actual object AppDatabaseConstructor :
    androidx.room.RoomDatabaseConstructor<bo.bordadoxdanny.app.data.database.AppDatabase> {
    actual override fun initialize(): bo.bordadoxdanny.app.data.database.AppDatabase {
        TODO("Not yet implemented")
    }
}