package bo.bordadoxdanny.app.features.profile.domain

class SendVerificationCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<String> {
        return repository.sendVerificationCode(email)
    }
}
