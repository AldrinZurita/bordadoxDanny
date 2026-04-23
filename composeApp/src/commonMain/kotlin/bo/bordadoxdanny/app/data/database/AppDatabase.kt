package bo.bordadoxdanny.app.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import bo.bordadoxdanny.app.features.orders.data.OrderEntity
import bo.bordadoxdanny.app.features.orders.data.OrderDao
import bo.bordadoxdanny.app.features.cash.data.CashEntity
import bo.bordadoxdanny.app.features.cash.data.CashDao
import bo.bordadoxdanny.app.features.reports.data.ReportEntity
import bo.bordadoxdanny.app.features.reports.data.ReportDao
import bo.bordadoxdanny.app.features.profile.data.ProfileEntity
import bo.bordadoxdanny.app.features.profile.data.ProfileDao
import bo.bordadoxdanny.app.features.config.data.ConfigEntity
import bo.bordadoxdanny.app.features.config.data.ConfigDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

@Database(
    entities = [
        OrderEntity::class,
        CashEntity::class,
        ReportEntity::class,
        ProfileEntity::class,
        ConfigEntity::class
    ],
    version = 2,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun cashDao(): CashDao
    abstract fun reportDao(): ReportDao
    abstract fun profileDao(): ProfileDao
    abstract fun configDao(): ConfigDao
}

// Room 2.7.0+ KMP standard: No incluir el cuerpo del método aquí
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

fun createRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver()) 
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}
