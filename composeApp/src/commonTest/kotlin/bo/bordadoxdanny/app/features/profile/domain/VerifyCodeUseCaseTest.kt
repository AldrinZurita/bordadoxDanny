package bo.bordadoxdanny.app.features.profile.domain

import bo.bordadoxdanny.app.fake.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class VerifyCodeUseCaseTest {

    @Test
    fun `given correct code, returns Success`() = runTest {
        val repo = FakeAuthRepository()
        // Simulated send code to set the expected code to "654321" in our Fake
        repo.sendVerificationCode("usuario@gmail.com")
        val useCase = VerifyCodeUseCase(repo)

        val result = useCase("usuario@gmail.com", "654321")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `given incorrect code, returns Failure`() = runTest {
        val repo = FakeAuthRepository()
        repo.sendVerificationCode("usuario@gmail.com")
        val useCase = VerifyCodeUseCase(repo)

        val result = useCase("usuario@gmail.com", "000000")

        assertTrue(result.isFailure)
    }
}
