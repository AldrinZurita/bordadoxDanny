package bo.bordadoxdanny.app.features.profile.presentation

import kotlinx.coroutines.test.runTest
import kotlin.test.*

class LanguageViewModelTest {

    @Test
    fun `given OnLoadLanguage intent with saved language, state is Loaded with that language`() = runTest {
        val fakePrefs = FakePreferencesRepository(savedLanguage = "es")
        val vm = LanguageViewModel(fakePrefs)

        vm.onIntent(LanguageIntent.OnLoadLanguage)

        assertTrue(vm.state.value is LanguageState.Loaded)
        val loaded = vm.state.value as LanguageState.Loaded
        assertEquals("es", loaded.languageCode)
    }

    @Test
    fun `given OnLoadLanguage intent with no saved language, state defaults to en`() = runTest {
        val fakePrefs = FakePreferencesRepository(savedLanguage = null)
        val vm = LanguageViewModel(fakePrefs)

        vm.onIntent(LanguageIntent.OnLoadLanguage)

        assertTrue(vm.state.value is LanguageState.Loaded)
        val loaded = vm.state.value as LanguageState.Loaded
        assertEquals("en", loaded.languageCode)
    }

    @Test
    fun `given OnLanguageChanged intent, state updates and saves language`() = runTest {
        val fakePrefs = FakePreferencesRepository(savedLanguage = "en")
        val vm = LanguageViewModel(fakePrefs)

        vm.onIntent(LanguageIntent.OnLanguageChanged("fr"))

        assertTrue(vm.state.value is LanguageState.Loaded)
        val loaded = vm.state.value as LanguageState.Loaded
        assertEquals("fr", loaded.languageCode)
        assertEquals("fr", fakePrefs.savedLanguage)
    }
}

class FakePreferencesRepository(var savedLanguage: String? = null) : bo.bordadoxdanny.app.data.preferences.PreferencesRepository {
    override suspend fun getLanguage(): String? = savedLanguage
    override suspend fun saveLanguage(languageCode: String) {
        savedLanguage = languageCode
    }
}
