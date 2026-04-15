package bo.bordadoxdanny.app.features.profile.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile?>
    suspend fun updateProfile(profile: Profile)
}
