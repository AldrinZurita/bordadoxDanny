package bo.bordadoxdanny.app.onboarding

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_prefs")

object OnboardingPreferences {
    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    suspend fun setOnboardingCompleted(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun isOnboardingCompleted(context: Context): Boolean {
        return context.dataStore.data
            .map { preferences ->
                preferences[ONBOARDING_COMPLETED] ?: false
            }
            .first()
    }

    suspend fun clearOnboarding(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(ONBOARDING_COMPLETED)
        }
    }
}
