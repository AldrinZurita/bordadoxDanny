package bo.bordadoxdanny.app.domain.profile

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile?>
    suspend fun updateProfile(profile: Profile)
}
