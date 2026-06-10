package bo.bordadoxdanny.app.data.preferences

interface PreferencesRepository {
    suspend fun getLanguage(userId: String): String?
    suspend fun saveLanguage(userId: String, languageCode: String)
}