package bo.bordadoxdanny.app.features.profile.presentation

import bo.bordadoxdanny.app.fake.FakeAuthRepository
import bo.bordadoxdanny.app.features.profile.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

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
    fun `given successful login intent, state becomes LoginSuccess`() = runTest {
        val repo = FakeAuthRepository(shouldReturnLoginError = false)
        val vm = AuthViewModel(
            loginUseCase = LoginUseCase(repo),
            registerUseCase = RegisterUseCase(repo),
            sendVerificationCodeUseCase = SendVerificationCodeUseCase(repo),
            verifyCodeUseCase = VerifyCodeUseCase(repo),
            resetPasswordUseCase = ResetPasswordUseCase(repo)
        )

        vm.onIntent(AuthIntent.OnLogin("usuario@gmail.com", "Usuario123"))

        assertEquals(AuthState.LoginSuccess, vm.state.value)
    }

    @Test
    fun `given failed login intent, state becomes LoginError`() = runTest {
        val repo = FakeAuthRepository(shouldReturnLoginError = true)
        val vm = AuthViewModel(
            loginUseCase = LoginUseCase(repo),
            registerUseCase = RegisterUseCase(repo),
            sendVerificationCodeUseCase = SendVerificationCodeUseCase(repo),
            verifyCodeUseCase = VerifyCodeUseCase(repo),
            resetPasswordUseCase = ResetPasswordUseCase(repo)
        )

        vm.onIntent(AuthIntent.OnLogin("bad@email.com", "wrongpass"))

        assertTrue(vm.state.value is AuthState.LoginError)
    }

    @Test
    fun `given password with lowercase, updates passwordRequirements`() = runTest {
        val repo = FakeAuthRepository()
        val vm = AuthViewModel(
            loginUseCase = LoginUseCase(repo),
            registerUseCase = RegisterUseCase(repo),
            sendVerificationCodeUseCase = SendVerificationCodeUseCase(repo),
            verifyCodeUseCase = VerifyCodeUseCase(repo),
            resetPasswordUseCase = ResetPasswordUseCase(repo)
        )

        vm.onIntent(AuthIntent.OnPasswordChanged("abc"))

        val reqs = vm.passwordRequirements.value
        assertTrue(reqs.hasLowercase)
        assertFalse(reqs.hasUppercase)
        assertFalse(reqs.hasNumber)
        assertFalse(reqs.hasMinLength)
    }

    @Test
    fun `given password meeting all requirements, allMet is true`() = runTest {
        val repo = FakeAuthRepository()
        val vm = AuthViewModel(
            loginUseCase = LoginUseCase(repo),
            registerUseCase = RegisterUseCase(repo),
            sendVerificationCodeUseCase = SendVerificationCodeUseCase(repo),
            verifyCodeUseCase = VerifyCodeUseCase(repo),
            resetPasswordUseCase = ResetPasswordUseCase(repo)
        )

        vm.onIntent(AuthIntent.OnPasswordChanged("Abc1abcd"))

        val reqs = vm.passwordRequirements.value
        assertTrue(reqs.hasLowercase)
        assertTrue(reqs.hasUppercase)
        assertTrue(reqs.hasNumber)
        assertTrue(reqs.hasMinLength)
        assertTrue(reqs.allMet)
    }
}
