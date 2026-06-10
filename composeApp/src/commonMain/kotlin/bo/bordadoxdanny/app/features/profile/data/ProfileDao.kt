package bo.bordadoxdanny.app.features.profile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Upsert // Usamos Upsert para simplificar insert/update
    suspend fun upsert(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE userId = :userId")
    fun getProfile(userId: String): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles WHERE userId = :userId")
    suspend fun getProfileSuspend(userId: String): ProfileEntity?
}
