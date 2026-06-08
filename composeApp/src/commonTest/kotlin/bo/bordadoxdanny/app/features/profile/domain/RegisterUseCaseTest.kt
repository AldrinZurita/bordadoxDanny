package bo.bordadoxdanny.app.features.profile.domain

import bo.bordadoxdanny.app.fake.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class RegisterUseCaseTest {

    private fun validParams() = RegisterParams(
        phoneCountryCode = "+591",
        phoneNumber = "71234567",
        username = "newuser",
        email = "new@email.com",
        password = "Passw0rd123",
        firstName = "Juan",
        middleName = null,
        lastName1 = "Perez",
        lastName2 = null,
        ciNumber = "1234567",
        ciComplement = null,
        ciDepartment = "LP"
    )

    @Test
    fun `given all valid data, returns Success`() = runTest {
        val repo = FakeAuthRepository()
        val useCase = RegisterUseCase(repo)

        val result = useCase(validParams())

        assertTrue(result.isSuccess)
    }

    @Test
    fun `given duplicate email, returns failure`() = runTest {
        val repo = FakeAuthRepository(duplicateEmail = "new@email.com")
        val useCase = RegisterUseCase(repo)

        val result = useCase(validParams())

        assertTrue(result.isFailure)
    }
}
