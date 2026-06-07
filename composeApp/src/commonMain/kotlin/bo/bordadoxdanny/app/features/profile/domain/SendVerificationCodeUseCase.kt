package bo.bordadoxdanny.app.features.profile.domain

class SendVerificationCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(emailOrUsername: String): Result<String> {
        return repository.sendVerificationCode(emailOrUsername)
    }
}
