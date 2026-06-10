package bo.bordadoxdanny.app.features.profile.domain

import kotlinx.coroutines.flow.Flow

class GetProfileUseCase(private val repository: ProfileRepository) {
    operator fun invoke(userId: String): Flow<Profile?> = repository.getProfile(userId)
}
