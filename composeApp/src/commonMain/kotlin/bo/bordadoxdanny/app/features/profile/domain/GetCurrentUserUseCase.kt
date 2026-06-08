package bo.bordadoxdanny.app.features.profile.domain

import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<User?> {
        return repository.getCurrentUser()
    }
}
