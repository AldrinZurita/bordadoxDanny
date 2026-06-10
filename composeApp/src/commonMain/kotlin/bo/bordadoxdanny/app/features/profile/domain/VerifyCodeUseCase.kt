package bo.bordadoxdanny.app.features.profile.domain

class VerifyCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, code: String): Result<Boolean> {
        if (code.length != 6 || !code.all { it.isDigit() }) {
            return Result.failure(IllegalArgumentException("Invalid code format"))
        }
        return repository.verifyCode(email, code)
    }
}
