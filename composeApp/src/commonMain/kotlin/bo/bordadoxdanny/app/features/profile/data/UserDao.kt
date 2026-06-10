package bo.bordadoxdanny.app.features.profile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getByIdSuspend(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE syncStatus = 'PENDING'")
    suspend fun getPendingUsers(): List<UserEntity>

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity)

    @Query("UPDATE users SET syncStatus = :status WHERE id = :userId")
    suspend fun updateSyncStatus(userId: String, status: String)

    @Query("UPDATE users SET isVerified = 1 WHERE id = :userId")
    suspend fun markAsVerified(userId: String)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
