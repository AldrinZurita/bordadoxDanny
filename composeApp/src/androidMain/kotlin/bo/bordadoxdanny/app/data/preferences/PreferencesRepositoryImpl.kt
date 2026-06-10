package bo.bordadoxdanny.app.data.preferences

class PreferencesRepositoryImpl(
    private val settingsDao: SettingsDao
) : PreferencesRepository {
    override suspend fun getLanguage(userId: String): String? {
        return settingsDao.getByKey("language", userId)?.value
    }

    override suspend fun saveLanguage(userId: String, languageCode: String) {
        settingsDao.save(SettingsEntity(key = "language", userId = userId, value = languageCode))
    }
}
