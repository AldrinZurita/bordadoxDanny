package bo.bordadoxdanny.app.core.locale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class AndroidLocaleManager : LocaleManager {
    override fun applyLocale(languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    override fun getCurrentLocale(): String {
        return AppCompatDelegate.getApplicationLocales().toLanguageTags().ifEmpty { "en" }
    }
}
