package bo.bordadoxdanny.app.features.profile.domain

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(emailOrUsername: String, password: String): Result<User> {
        if (emailOrUsername.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Fields cannot be empty"))
        }
        return repository.login(emailOrUsername, password)
    }
}
