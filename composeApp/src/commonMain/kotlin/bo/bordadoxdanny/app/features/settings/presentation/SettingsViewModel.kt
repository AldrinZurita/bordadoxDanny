package bo.bordadoxdanny.app.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.settings.data.UserPreferencesDao
import bo.bordadoxdanny.app.features.settings.domain.UserPreferences
import bo.bordadoxdanny.app.features.notifications.TranslationService
import bo.bordadoxdanny.app.util.Notifier
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dao: UserPreferencesDao,
    private val translationService: TranslationService,
    private val notifier: Notifier
) : ViewModel() {
    
    val preferences = dao.getPreferencesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    fun updateLanguage(code: String) {
        viewModelScope.launch {
            dao.upsertPreferences(UserPreferences(languageCode = code))
        }
    }

    fun triggerTest() {
        viewModelScope.launch {
            try {
                val currentLang = dao.getPreferencesSync()?.languageCode ?: "en-US"
                
                // We use your EXACT Asset IDs from Localise.biz
                val idTitle = "title" 
                val idBody = "body"

                val tTitle = translationService.translate(idTitle, currentLang)
                val tBody = translationService.translate(idBody, currentLang)
                
                // Show the result
                notifier.showNotification(tTitle, tBody)
                
            } catch (e: Exception) {
                println("BORDADOS_TEST: ERROR: ${e.message}")
            }
        }
    }
}
