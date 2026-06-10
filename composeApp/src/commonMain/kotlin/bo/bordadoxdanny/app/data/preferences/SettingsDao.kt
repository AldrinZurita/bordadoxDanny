package bo.bordadoxdanny.app.data.preferences

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE key = :key AND userId = :userId LIMIT 1")
    suspend fun getByKey(key: String, userId: String): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: SettingsEntity)

    @Query("DELETE FROM settings WHERE key = :key AND userId = :userId")
    suspend fun delete(key: String, userId: String)
}