package bo.bordadoxdanny.app.features.profile.domain

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(params: RegisterParams): Result<User> {
        return repository.register(params)
    }
}
