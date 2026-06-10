package bo.bordadoxdanny.app.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import bo.bordadoxdanny.app.features.orders.data.OrderEntity
import bo.bordadoxdanny.app.features.orders.data.OrderDao
import bo.bordadoxdanny.app.features.cash.data.TransactionEntity
import bo.bordadoxdanny.app.features.cash.data.TransactionDao
import bo.bordadoxdanny.app.features.reports.data.ReportEntity
import bo.bordadoxdanny.app.features.reports.data.ReportDao
import bo.bordadoxdanny.app.features.reports.data.ReportSummaryEntity
import bo.bordadoxdanny.app.features.reports.data.ReportSummaryDao
import bo.bordadoxdanny.app.features.profile.data.ProfileEntity
import bo.bordadoxdanny.app.features.profile.data.ProfileDao
import bo.bordadoxdanny.app.features.profile.data.UserEntity
import bo.bordadoxdanny.app.features.profile.data.UserDao
import bo.bordadoxdanny.app.data.preferences.SettingsEntity
import bo.bordadoxdanny.app.data.preferences.SettingsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

@Database(
    entities = [
        OrderEntity::class,
        TransactionEntity::class,
        ReportEntity::class,
        ReportSummaryEntity::class,
        ProfileEntity::class,
        UserEntity::class,
        SettingsEntity::class
    ],
    version = 5
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun transactionDao(): TransactionDao
    abstract fun reportDao(): ReportDao
    abstract fun reportSummaryDao(): ReportSummaryDao
    abstract fun profileDao(): ProfileDao
    abstract fun userDao(): UserDao
    abstract fun settingsDao(): SettingsDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

fun createRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
