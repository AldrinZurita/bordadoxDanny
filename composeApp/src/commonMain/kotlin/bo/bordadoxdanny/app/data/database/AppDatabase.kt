package bo.bordadoxdanny.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import bo.bordadoxdanny.app.data.orders.OrderEntity
import bo.bordadoxdanny.app.data.orders.OrderDao
import bo.bordadoxdanny.app.data.cash.CashEntity
import bo.bordadoxdanny.app.data.cash.CashDao
import bo.bordadoxdanny.app.data.reports.ReportEntity
import bo.bordadoxdanny.app.data.reports.ReportDao
import bo.bordadoxdanny.app.data.profile.ProfileEntity
import bo.bordadoxdanny.app.data.profile.ProfileDao

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
