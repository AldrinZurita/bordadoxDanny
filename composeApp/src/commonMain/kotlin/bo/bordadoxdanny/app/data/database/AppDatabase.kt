package bo.bordadoxdanny.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import bo.bordadoxdanny.app.features.orders.data.OrderEntity
import bo.bordadoxdanny.app.features.orders.data.OrderDao
import bo.bordadoxdanny.app.features.cash.data.CashEntity
import bo.bordadoxdanny.app.features.cash.data.CashDao
import bo.bordadoxdanny.app.features.reports.data.ReportEntity
import bo.bordadoxdanny.app.features.reports.data.ReportDao
import bo.bordadoxdanny.app.features.profile.data.ProfileEntity
import bo.bordadoxdanny.app.features.profile.data.ProfileDao

@Database(
    entities = [
        OrderEntity::class,
        CashEntity::class,
        ReportEntity::class,
        ProfileEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun cashDao(): CashDao
    abstract fun reportDao(): ReportDao
    abstract fun profileDao(): ProfileDao

    companion object
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
