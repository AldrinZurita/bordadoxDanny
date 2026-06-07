package bo.bordadoxdanny.app.features.profile.presentation

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class LanguageViewModelTest {

    @Test
    fun `given OnLoadLanguage intent with saved language, state is Loaded with that language`() = runTest {
        val fakePrefs = FakePreferencesRepository(savedLanguage = "es")
        val vm = LanguageViewModel(fakePrefs)

        vm.state.test {
            // Skip initial Loading state if it's there, or wait for Loaded
            var item = awaitItem()
            if (item is LanguageState.Loading) {
                item = awaitItem()
            }
            
            assertTrue(item is LanguageState.Loaded)
            assertEquals("es", (item as LanguageState.Loaded).languageCode)
        }
    }

    @Test
    fun `given OnLoadLanguage intent with no saved language, state defaults to en`() = runTest {
        val fakePrefs = FakePreferencesRepository(savedLanguage = null)
        val vm = LanguageViewModel(fakePrefs)

        vm.state.test {
            var item = awaitItem()
            if (item is LanguageState.Loading) {
                item = awaitItem()
            }

            assertTrue(item is LanguageState.Loaded)
            assertEquals("en", (item as LanguageState.Loaded).languageCode)
        }
    }

    @Test
    fun `given OnLanguageChanged intent, state updates and saves language`() = runTest {
        val fakePrefs = FakePreferencesRepository(savedLanguage = "en")
        val vm = LanguageViewModel(fakePrefs)

        // Wait for initial load to finish first
        vm.state.test {
            if (awaitItem() is LanguageState.Loading) awaitItem()
            
            vm.onIntent(LanguageIntent.OnLanguageChanged("fr"))
            
            val item = awaitItem()
            assertTrue(item is LanguageState.Loaded)
            assertEquals("fr", (item as LanguageState.Loaded).languageCode)
            assertEquals("fr", fakePrefs.savedLanguage)
        }
    }
}

class FakePreferencesRepository(var savedLanguage: String? = null) : bo.bordadoxdanny.app.data.preferences.PreferencesRepository {
    override suspend fun getLanguage(): String? = savedLanguage
    override suspend fun saveLanguage(languageCode: String) {
        savedLanguage = languageCode
    }
}
