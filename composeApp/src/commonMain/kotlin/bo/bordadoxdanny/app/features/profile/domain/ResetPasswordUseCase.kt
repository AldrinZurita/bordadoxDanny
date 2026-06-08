package bo.bordadoxdanny.app.features.profile.domain

class ResetPasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, newPassword: String): Result<Unit> {
        return repository.resetPassword(email, newPassword)
    }
}
