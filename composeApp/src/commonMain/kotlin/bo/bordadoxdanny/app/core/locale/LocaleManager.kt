package bo.bordadoxdanny.app.core.locale

interface LocaleManager {
    fun applyLocale(languageCode: String)
    fun getCurrentLocale(): String
}
