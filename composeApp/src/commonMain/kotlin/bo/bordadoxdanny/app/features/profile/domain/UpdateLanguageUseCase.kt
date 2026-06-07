package bo.bordadoxdanny.app.features.profile.domain

class UpdateLanguageUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(userId: String, languageCode: String): Result<Unit> {
        return repository.updateLanguage(userId, languageCode)
    }
}
