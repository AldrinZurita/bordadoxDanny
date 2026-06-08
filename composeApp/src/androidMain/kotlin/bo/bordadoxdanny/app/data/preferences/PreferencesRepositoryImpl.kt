package bo.bordadoxdanny.app.data.preferences

class PreferencesRepositoryImpl(
    private val settingsDao: SettingsDao
) : PreferencesRepository {
    override suspend fun getLanguage(): String? {
        return settingsDao.getByKey("language")?.value
    }

    override suspend fun saveLanguage(languageCode: String) {
        settingsDao.save(SettingsEntity(key = "language", value = languageCode))
    }
}