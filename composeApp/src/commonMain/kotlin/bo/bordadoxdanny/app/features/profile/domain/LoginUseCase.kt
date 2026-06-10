package bo.bordadoxdanny.app.features.profile.domain

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Fields cannot be empty"))
        }
        return repository.login(email, password)
    }
}
