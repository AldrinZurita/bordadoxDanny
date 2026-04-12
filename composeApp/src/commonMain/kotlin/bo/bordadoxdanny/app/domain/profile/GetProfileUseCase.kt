package bo.bordadoxdanny.app.domain.profile

import kotlinx.coroutines.flow.Flow

class GetProfileUseCase(private val repository: ProfileRepository) {
    operator fun invoke(): Flow<Profile?> = repository.getProfile()
}
