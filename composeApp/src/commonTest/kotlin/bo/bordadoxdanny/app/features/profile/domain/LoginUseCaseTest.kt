package bo.bordadoxdanny.app.features.profile.domain

import bo.bordadoxdanny.app.fake.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginUseCaseTest {

    @Test
    fun `given valid credentials, returns Success with User`() = runTest {
        val repo = FakeAuthRepository(shouldReturnLoginError = false)
        val useCase = LoginUseCase(repo)

        val result = useCase("usuario@gmail.com", "Usuario123")

        assertTrue(result.isSuccess)
        assertEquals("usuario@gmail.com", result.getOrThrow().email)
    }

    @Test
    fun `given invalid credentials, returns Failure`() = runTest {
        val repo = FakeAuthRepository(shouldReturnLoginError = true)
        val useCase = LoginUseCase(repo)

        val result = useCase("wrong@email.com", "wrongpass")

        assertTrue(result.isFailure)
    }

    @Test
    fun `given empty emailOrUsername, returns Failure`() = runTest {
        val repo = FakeAuthRepository()
        val useCase = LoginUseCase(repo)

        val result = useCase("", "Password123")

        assertTrue(result.isFailure)
    }
}
