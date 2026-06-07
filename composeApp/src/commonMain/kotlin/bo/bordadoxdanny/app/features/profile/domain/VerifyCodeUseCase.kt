package bo.bordadoxdanny.app.features.profile.domain

class VerifyCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, code: String): Result<Unit> {
        return repository.verifyCode(email, code)
    }
}
