package bo.bordadoxdanny.app.features.profile.data

import bo.bordadoxdanny.app.features.profile.domain.Profile
import bo.bordadoxdanny.app.features.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(private val dao: ProfileDao) : ProfileRepository {
    override fun getProfile(): Flow<Profile?> = dao.getProfile().map { entity ->
        entity?.let { Profile(it.id, it.name, it.email, it.phone) }
    }

    override suspend fun updateProfile(profile: Profile) {
        dao.update(ProfileEntity(id = profile.id, name = profile.name, email = profile.email, phone = profile.phone))
    }
}
