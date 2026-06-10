package bo.bordadoxdanny.app.features.profile.domain

class RegisterUseCase(private val repository: AuthRepository) {
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")

    suspend operator fun invoke(params: RegisterParams, confirmPassword: String): Result<User> {
        if (!emailRegex.matches(params.email)) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        
        if (!validatePassword(params.password)) {
            return Result.failure(IllegalArgumentException("Password does not meet requirements"))
        }

        if (params.password != confirmPassword) {
            return Result.failure(IllegalArgumentException("Passwords do not match"))
        }

        return repository.register(params)
    }

    private fun validatePassword(password: String): Boolean {
        if (password.length < 8) return false
        if (!password.any { it.isUpperCase() }) return false
        if (!password.any { it.isDigit() }) return false
        if (!password.any { !it.isLetterOrDigit() }) return false
        return true
    }
}
