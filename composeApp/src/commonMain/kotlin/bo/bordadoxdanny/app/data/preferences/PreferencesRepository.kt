package bo.bordadoxdanny.app.data.preferences

interface PreferencesRepository {
    suspend fun getLanguage(): String?
    suspend fun saveLanguage(languageCode: String)
}