package bo.bordadoxdanny.app.data.preferences

class PreferencesRepositoryImpl : PreferencesRepository {
    override suspend fun getLanguage(): String? {
        return "en"
    }

    override suspend fun saveLanguage(languageCode: String) {
        // iOS: Would use NSUserDefaults
    }
}