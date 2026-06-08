package bo.bordadoxdanny.app.features.profile.presentation

import bo.bordadoxdanny.app.fake.FakeAuthRepository
import bo.bordadoxdanny.app.features.profile.domain.GetCurrentUserUseCase
import bo.bordadoxdanny.app.features.profile.domain.LogoutUseCase
import bo.bordadoxdanny.app.features.profile.domain.UpdateLanguageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initially loads user and transitions to Success`() = runTest {
        val repo = FakeAuthRepository()
        val vm = ProfileViewModel(
            getCurrentUserUseCase = GetCurrentUserUseCase(repo),
            updateLanguageUseCase = UpdateLanguageUseCase(repo),
            logoutUseCase = LogoutUseCase(repo)
        )

        assertTrue(vm.state.value is ProfileState.Success)
        val success = vm.state.value as ProfileState.Success
        assertEquals("usuario@gmail.com", success.user.email)
    }

    @Test
    fun `given OnLogoutConfirmed intent, state transitions to LoggedOut`() = runTest {
        val repo = FakeAuthRepository()
        val vm = ProfileViewModel(
            getCurrentUserUseCase = GetCurrentUserUseCase(repo),
            updateLanguageUseCase = UpdateLanguageUseCase(repo),
            logoutUseCase = LogoutUseCase(repo)
        )

        vm.onIntent(ProfileIntent.OnLogoutConfirmed)

        assertEquals(ProfileState.LoggedOut, vm.state.value)
    }

    @Test
    fun `given OnLogoutClicked intent, showLogoutDialog becomes true`() = runTest {
        val repo = FakeAuthRepository()
        val vm = ProfileViewModel(
            getCurrentUserUseCase = GetCurrentUserUseCase(repo),
            updateLanguageUseCase = UpdateLanguageUseCase(repo),
            logoutUseCase = LogoutUseCase(repo)
        )

        vm.onIntent(ProfileIntent.OnLogoutClicked)

        assertTrue(vm.showLogoutDialog.value)
    }
}
