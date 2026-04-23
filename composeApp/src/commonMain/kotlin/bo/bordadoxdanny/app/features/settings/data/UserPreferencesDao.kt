package bo.bordadoxdanny.app.features.settings.data

import androidx.room.*
import bo.bordadoxdanny.app.features.settings.domain.UserPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    @Upsert
    suspend fun upsertPreferences(prefs: UserPreferences)

    @Query("SELECT * FROM user_preferences WHERE id = 0")
    fun getPreferencesFlow(): Flow<UserPreferences?>

    @Query("SELECT * FROM user_preferences WHERE id = 0")
    suspend fun getPreferencesSync(): UserPreferences?
}
